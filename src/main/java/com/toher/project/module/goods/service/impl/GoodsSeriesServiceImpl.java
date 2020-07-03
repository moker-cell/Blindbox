package com.toher.project.module.goods.service.impl;

import com.toher.project.module.goods.entity.GoodsSeries;
import com.toher.project.module.goods.service.GoodsSeriesService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class GoodsSeriesServiceImpl implements GoodsSeriesService {

    @Override
    public GoodsSeries findObjectByPrimaryKey(String o) {
        return null;
    }

    @Override
    public GoodsSeries findObjectByEntity(GoodsSeries goodsSeries) {
        return null;
    }

    @Override
    public List<GoodsSeries> getObjects(Map<String, Object> map) {
        return null;
    }

    @Override
    public List<GoodsSeries> getObjectsByEntity(GoodsSeries goodsSeries) {
        return null;
    }

    @Override
    public int saveObject(GoodsSeries goodsSeries) {
        return 0;
    }

    @Override
    public int editObject(GoodsSeries goodsSeries) {
        return 0;
    }

    @Override
    public int saveObjectSelective(GoodsSeries goodsSeries) {
        return 0;
    }

    @Override
    public int editObjectSelective(GoodsSeries goodsSeries) {
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
