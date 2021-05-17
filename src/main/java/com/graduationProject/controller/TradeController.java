package com.graduationProject.controller;

import com.alibaba.fastjson.JSON;
import com.graduationProject.consts.StatusCode;
import com.graduationProject.dto.Page;
import com.graduationProject.dto.ResultDTO;
import com.graduationProject.entity.Trade;
import com.graduationProject.entity.User;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * TradeController
 * <p>
 * 交易记录控制类
 * <p>
 * Created : 2021/5/14 15:53
 *
 * @author : Niaowuuu
 * @version : 1.0
 */
@RestController
@RequestMapping("/strategy/{id}/trade")
public class TradeController {

    @GetMapping("/list")
    // public ResultDTO<List<Strategy>> getAllStrategies(@RequestBody Map map) throws IOException, ContractException {
    public ResultDTO<Page<List<Trade>>> getTradesPageByStrategyID(@PathVariable("id") String id) {
        try {
            // String userName = (String) map.get("userName");
            // String userSecret = (String) map.get("userSecret");
            User user = new User("user1", "user1pw", "Subscriber");
            user.doEnroll();
            byte[] result = user.doQuery("GetTradesByStrategyID", id, "");
            if (result.length != 0) {
                Page<List<Trade>> trades = Trade.deserializePage(result);
                System.out.println(trades);
                return new ResultDTO<>(trades);
            }
            return new ResultDTO<>(new Page<>(new ArrayList<>(), ""));
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultDTO<>(StatusCode.INTERNAL_ERROR);
        }
    }

    @GetMapping("")
    public ResultDTO<?> delTrades(@PathVariable String id) {
        try {
            User user = new User("user1", "user1pw", "Subscriber");
            user.doEnroll();
            byte[] result = user.doQuery("GetTradesByStrategyID", id, "");
            while (result.length != 0) {
                Page<List<Trade>> trades = Trade.deserializePage(result);
                System.out.println(trades);
                user.doInvoke("DelTradesByStrategyID", JSON.toJSONString(trades.getData()));
                result = user.doQuery("GetTradesByStrategyID", id, "");
            }
            return new ResultDTO<>(StatusCode.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultDTO<>(StatusCode.INTERNAL_ERROR);
        }
    }
}