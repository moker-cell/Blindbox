/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toher.common.execl;

import com.github.crab2died.converter.WriteConvertible;
import com.github.crab2died.utils.DateUtils;
import java.util.Date;

/**
 *
 * @author Administrator
 */
public class DateByYYYYMMDDConverter implements WriteConvertible {

    @Override
    public Object execWrite(Object object) {
        if (object != null) {
            Date date = (Date) object;
            return DateUtils.date2Str(date, DateUtils.DATE_FORMAT_DAY);
        }
        return null;
    }

}
