package com.graduationProject.dto;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;


/**
 * Strategy
 *
 * 投资策略实体类
 *
 * Updated : 2021/5/14 0:42
 * @author : Niaowuuu
 * @version : 1.0
 */

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class StrategyDTO extends State {


    // 策略ID
    private String id;

    // 策略名
    private String name;

    // 发布者
    private String provider;

    // 最大回撤
    private Double maxDrawdown;

    // 年化收益率
    private Double annualReturn;

    // 夏普率
    private Double sharpeRatio;

    // 是否公开
    private Integer state;

    // 策略订阅者的用户凭证
    // private List<String> subscribers;

    private Boolean isSub;

    public static StrategyDTO deserialize(byte[] data) {
        JSONObject json = JSON.parseObject(new String(data, UTF_8));
        return deserialize(json);
    }

    public static StrategyDTO deserialize(JSONObject json) {

        String id = json.getString("id");
        String name = json.getString("name");
        String provider = json.getString("provider");
        Double maxDrawdown = json.getDouble("maxDrawdown");
        Double annualReturn = json.getDouble("annualReturn");
        Double sharpRatio = json.getDouble("sharpeRatio");
        // List<String> subscribers = new ArrayList<>();
        // JSONArray subscribersArray = json.getJSONArray("subscribers");
        // for (int i = 0; i < subscribersArray.size(); i++) {
        //     subscribers.add(subscribersArray.getString(i));
        // }
        Boolean isSub = json.getBoolean("isSub");

        Integer state = json.getInteger("state");
        return createInstance(id, name, provider, maxDrawdown, annualReturn, sharpRatio, state, isSub);
    }

    public static List<StrategyDTO> deserializeList(byte[] data) {
        JSONArray json = JSON.parseArray(new String(data, UTF_8));
        List<StrategyDTO> strategies = new ArrayList<>();
        for (int i = 0; i < json.size(); i++) {
            strategies.add(deserialize(json.getJSONObject(i)));
        }

        return strategies;
    }


    public static StrategyDTO createInstance(String id,
                                             String name,
                                             String provider,
                                             Double maxDrawdown,
                                             Double annualReturn,
                                             Double sharpeRatio,
                                             Integer state,
                                             Boolean isSub
    ) {
        return new StrategyDTO()
                .setId(id)
                .setName(name)
                .setProvider(provider)
                .setMaxDrawdown(maxDrawdown)
                .setAnnualReturn(annualReturn)
                .setSharpeRatio(sharpeRatio)
                .setState(state)
                .setIsSub(isSub);
    }
}