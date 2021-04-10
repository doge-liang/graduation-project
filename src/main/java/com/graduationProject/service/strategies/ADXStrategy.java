package com.graduationProject.service.strategies;

import org.ta4j.core.*;
import org.ta4j.core.analysis.criteria.TotalProfitCriterion;
import org.ta4j.core.indicators.SMAIndicator;
import org.ta4j.core.indicators.adx.ADXIndicator;
import org.ta4j.core.indicators.adx.MinusDIIndicator;
import org.ta4j.core.indicators.adx.PlusDIIndicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.trading.rules.CrossedDownIndicatorRule;
import org.ta4j.core.trading.rules.CrossedUpIndicatorRule;
import org.ta4j.core.trading.rules.OverIndicatorRule;
import org.ta4j.core.trading.rules.UnderIndicatorRule;
import ta4jexamples.loaders.CsvTradesLoader;

public class ADXStrategy {

    /**
     * @param series a bar series
     * @return an adx indicator based strategy
     */
    public static Strategy buildStrategy(BarSeries series) {
        if (series == null) {
            throw new IllegalArgumentException("Series cannot be null");
        }

        final ClosePriceIndicator closePriceIndicator = new ClosePriceIndicator(series);
        final SMAIndicator smaIndicator = new SMAIndicator(closePriceIndicator, 50);

        final int adxBarCount = 14;
        final ADXIndicator adxIndicator = new ADXIndicator(series, adxBarCount);
        final OverIndicatorRule adxOver20Rule = new OverIndicatorRule(adxIndicator, 20);

        final PlusDIIndicator plusDIIndicator = new PlusDIIndicator(series, adxBarCount);
        final MinusDIIndicator minusDIIndicator = new MinusDIIndicator(series, adxBarCount);

        final Rule plusDICrossedUpMinusDI = new CrossedUpIndicatorRule(plusDIIndicator, minusDIIndicator);
        final Rule plusDICrossedDownMinusDI = new CrossedDownIndicatorRule(plusDIIndicator, minusDIIndicator);
        final OverIndicatorRule closePriceOverSma = new OverIndicatorRule(closePriceIndicator, smaIndicator);
        final Rule entryRule = adxOver20Rule.and(plusDICrossedUpMinusDI).and(closePriceOverSma);

        final UnderIndicatorRule closePriceUnderSma = new UnderIndicatorRule(closePriceIndicator, smaIndicator);
        final Rule exitRule = adxOver20Rule.and(plusDICrossedDownMinusDI).and(closePriceUnderSma);

        return new BaseStrategy("ADXStrategy", entryRule, exitRule, adxBarCount);
    }

    public static void main(String[] args) {

        // Getting the bar series
        BarSeries series = CsvTradesLoader.loadBitstampSeries();

        // Building the trading strategy
        Strategy strategy = buildStrategy(series);

        // Running the strategy
        BarSeriesManager seriesManager = new BarSeriesManager(series);
        TradingRecord tradingRecord = seriesManager.run(strategy);
        System.out.println("Number of trades for the strategy: " + tradingRecord.getTradeCount());

        // Analysis
        System.out.println(
                "Total profit for the strategy: " + new TotalProfitCriterion().calculate(series, tradingRecord));
    }
}
