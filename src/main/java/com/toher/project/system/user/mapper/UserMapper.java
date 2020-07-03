
package com.toher.project.system.user.mapper;


import com.toher.framework.mapper.MyMapper;
import com.toher.project.system.user.entity.User;

import java.util.List;
import java.util.Map;

/**
 * @Author: 同恒科技-李怀明
 * @Date: 2018/12/4 15:19
 */

public interface UserMapper extends MyMapper<User> {
    List<User> getObjectsInXML(Map<String, Object> map);
    User selectUserAndRolesInXML(String userId);
}
