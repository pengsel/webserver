package com.pengsel.ws.hs.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.pengsel.ws.hs.Dispatcher;
import com.pengsel.ws.rpc.JsonRpc;

import java.io.IOException;
import java.math.BigInteger;
import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.pengsel.ws.util.Constants.REGISTER_CENTER_PORT;
import static com.pengsel.ws.util.Constants.RIGISTER_CENTER_IP;

public class HTTPDispatcher implements Dispatcher {


    private ConcurrentHashMap<String, DispatchData> dispatchData = new ConcurrentHashMap<String, DispatchData>();
    private DispatchStratege stratege;



    public void init() {


        Object result= null;
        try {
            result = JsonRpc.call("service",null,new Socket(RIGISTER_CENTER_IP,REGISTER_CENTER_PORT));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Map map=JSON.parseObject(result.toString(),Map.class);
        Iterator iterator=map.keySet().iterator();
        while (iterator.hasNext()){
            Map.Entry entry=(Map.Entry) iterator.next();
            String key= (String) entry.getKey();
            String values= (String) entry.getValue();
            if (dispatchData.contains(key)){
                //FIXME 此处规定了传过来的数据为 IP地址 端口号 权重 eg：127.0.0.1 80 5 ;198.212.123.21 65 10
                String[] valueArray=values.split(";");
                List<InetSocketAddress> list = dispatchData.get(key).services;
                List<Integer> weights = dispatchData.get(key).weights;
                for (String value:valueArray) {
                    String[] strings = value.split(" ");
                    InetSocketAddress address = new InetSocketAddress(strings[0], Integer.valueOf(strings[1]));
                    list.add(address);
                    weights.add(Integer.valueOf(strings[2]));
                }
            }else {

                String[] valueArray=values.split(";");
                List<InetSocketAddress> list = new ArrayList<InetSocketAddress>();
                List<Integer> weights=new ArrayList<Integer>();
                for (String value:valueArray) {

                    String[] strings = value.split(" ");
                    InetSocketAddress address = new InetSocketAddress(strings[0], Integer.valueOf(strings[1]));

                    list.add(address);
                    weights.add(Integer.valueOf(strings[2]));

                }
                DispatchData data = new DispatchData(list, weights);
                dispatchData.put(key, data);

            }
        }
    }

    public void configure(DispatchStratege stratege) {

        this.stratege=stratege;
    }

    public InetSocketAddress getAddr(String uri,String host) {

        DispatchData data= dispatchData.get(uri);
        InetSocketAddress address=null;
        switch (stratege){

            case POLLING:
                int pos=data.curPos%data.services.size();
                address=data.services.get(pos);
                data.curPos=pos+1;
                break;
            case WEIGHTED_POLLING:
                //每次减去最大公约数
                if (data.curWeight<=0){
                    data.curPos++;
                    address=data.services.get(data.curPos);
                    data.curWeight=data.weights.get(data.curPos);
                }else {
                    data.curWeight-=data.greatestConmmonDivisor;
                    address=data.services.get(data.curPos);
                }
                break;

            case IP_HASH:
                String[] strings=host.split(".");
                int sum=0;
                for (String s:strings){
                    sum+=Integer.valueOf(s);
                }
                address=data.services.get(sum%data.services.size());
                break;
        }
        return address;
    }


    private class DispatchData{
        //存储多少个TCPServer地址
        List<InetSocketAddress>  services;

        //权重
        List<Integer> weights;

        //当前位置
        int curPos=0;

        //当前权重
        int curWeight=weights.get(curPos);

        int greatestConmmonDivisor;


        public DispatchData(List<InetSocketAddress> services, List<Integer> weights) {
            this.services = services;
            this.weights = weights;
            getGreatestConmmonDivisor(weights);
        }


        void getGreatestConmmonDivisor(List<Integer> list){

            int commonDivisor=1;
            for (int i=0;i<list.size()-1;i++){
                if (list.get(i)!=0){
                    commonDivisor=gcd(list.get(i),list.get(i+1));
                }
            }
            greatestConmmonDivisor=commonDivisor;
        }

        /**
         * 求两个数的最大公约数 4和6最大公约数是2
         *
         * @param num1
         * @param num2
         * @return
         */
        private int gcd(int num1, int num2) {
            BigInteger i1 = new BigInteger(String.valueOf(num1));
            BigInteger i2 = new BigInteger(String.valueOf(num2));
            return i1.gcd(i2).intValue();
        }
    }


    public enum DispatchStratege{
        POLLING,
        WEIGHTED_POLLING,
        IP_HASH
    }
}
