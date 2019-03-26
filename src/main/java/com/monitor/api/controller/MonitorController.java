package com.monitor.api.controller;

import com.monitor.api.entity.CollectData;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "monitor")
public class MonitorController {

    @RequestMapping("get")
    public String getCollectData(Model model) {
        List<Map<String, Object>> list = new ArrayList<>();

        Map<String, List<Long>> data = CollectData.data;
        for (Map.Entry<String, List<Long>> entry : data.entrySet()) {
            Map<String, Object> map = new HashMap<>();
            String key = entry.getKey();
            List<Long> value = entry.getValue();
            int sum = value.stream().mapToInt(Long::intValue).sum();
            BigDecimal avg = BigDecimal.valueOf(sum).divide(BigDecimal.valueOf(value.size()), 2, BigDecimal.ROUND_UP);

            map.put("key", key);
            map.put("avg", avg);
            map.put("size", value.size());

            list.add(map);
        }

        Map<String, Object> temp;
        for (int i=0; i<list.size(); i++) {
            for (int k=i+1; k<list.size(); k++) {
                Map<String, Object> im = list.get(i);
                Map<String, Object> km = list.get(k);
                BigDecimal iavg = (BigDecimal) im.get("avg");
                BigDecimal kavg = (BigDecimal) km.get("avg");

                if (iavg.intValue() < kavg.intValue()) {
                    temp = im;
                    list.set(i, list.get(k));
                    list.set(k, temp);
                }
            }
        }

        model.addAttribute("data", list);

        return "index";
    }


}
