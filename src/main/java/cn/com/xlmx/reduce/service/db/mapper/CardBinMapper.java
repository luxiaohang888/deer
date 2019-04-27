package cn.com.xlmx.reduce.service.db.mapper;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author luxiaohang
 * @Title:
 * @Description:
 * @date 2019/04/27/20:19
 **/
@Repository
@Transactional(rollbackFor = Exception.class)
public interface CardBinMapper {
    /**
     * 有则更新，无则新增CCMS_F401
     *
     * @param list
     */
    void updatetableforccmsF401(List<Object> list);

}

