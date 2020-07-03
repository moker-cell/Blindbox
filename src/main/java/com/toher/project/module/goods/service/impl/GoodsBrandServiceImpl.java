package com.toher.project.module.goods.service.impl;

import com.toher.project.module.goods.entity.GoodsBrand;
import com.toher.project.module.goods.service.GoodsBrandService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class GoodsBrandServiceImpl implements GoodsBrandService {
    @Override
    public GoodsBrand findObjectByPrimaryKey(String o) {
        return null;
    }

    @Override
    public GoodsBrand findObjectByEntity(GoodsBrand goodsBrand) {
        return null;
    }

    @Override
    public List<GoodsBrand> getObjects(Map<String, Object> map) {
        return null;
    }

    @Override
    public List<GoodsBrand> getObjectsByEntity(GoodsBrand goodsBrand) {
        return null;
    }

    @Override
    public int saveObject(GoodsBrand goodsBrand) {
        return 0;
    }

    @Override
    public int editObject(GoodsBrand goodsBrand) {
        return 0;
    }

    @Override
    public int saveObjectSelective(GoodsBrand goodsBrand) {
        return 0;
    }

    @Override
    public int editObjectSelective(GoodsBrand goodsBrand) {
        return 0;
    }

    @Override
    public int deleteObjectByPrimaryKey(String o) {
        return 0;
    }

    @Override
    public int deleteBatchObject(List<String> data) {
        return 0;
    }
}
