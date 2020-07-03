package com.toher.project.module.goods.service.impl;

import com.toher.project.module.goods.entity.GoodsClassification;
import com.toher.project.module.goods.service.GoodsClassificationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class GoodsClassificationServiceImpl implements GoodsClassificationService {
    @Override
    public GoodsClassification findObjectByPrimaryKey(Integer o) {
        return null;
    }

    @Override
    public GoodsClassification findObjectByEntity(GoodsClassification goodsClassification) {
        return null;
    }

    @Override
    public List<GoodsClassification> getObjects(Map<String, Object> map) {
        return null;
    }

    @Override
    public List<GoodsClassification> getObjectsByEntity(GoodsClassification goodsClassification) {
        return null;
    }

    @Override
    public int saveObject(GoodsClassification goodsClassification) {
        return 0;
    }

    @Override
    public int editObject(GoodsClassification goodsClassification) {
        return 0;
    }

    @Override
    public int saveObjectSelective(GoodsClassification goodsClassification) {
        return 0;
    }

    @Override
    public int editObjectSelective(GoodsClassification goodsClassification) {
        return 0;
    }

    @Override
    public int deleteObjectByPrimaryKey(Integer o) {
        return 0;
    }

    @Override
    public int deleteBatchObject(List<Integer> data) {
        return 0;
    }
}
