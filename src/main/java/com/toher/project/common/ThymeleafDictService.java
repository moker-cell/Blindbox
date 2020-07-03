package com.toher.project.common;

import com.toher.project.system.dict.entity.DictData;
import com.toher.project.system.dict.service.DictDataService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: 同恒科技-李怀明
 * @Date: 2018/12/24 11:31
 */
@Component
public class ThymeleafDictService {

    @Resource
    private DictDataService dictDataServiceImpl;

    /**
     * 根据字典类型查询字典数据信息
     * @param dictType 字典类型
     * @return 参数键值
     */
    public List<DictData> selectDictData(String dictType){
        DictData dictData = new DictData();
        dictData.setDictType(dictType);
        dictData.setStatus(true);
        return dictDataServiceImpl.getObjectsByEntity(dictData);
    }

}
