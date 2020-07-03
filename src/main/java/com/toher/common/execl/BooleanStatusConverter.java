/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toher.common.execl;

import com.github.crab2died.converter.WriteConvertible;

/**
 *
 * @author Administrator
 */
public class BooleanStatusConverter implements WriteConvertible {

    @Override
    public Object execWrite(Object object) {
        if (object != null) {
            if (object.equals(true)) {
                return "是";
            }
        }
        return "否";
    }

}
