package com.thinkingdata.webui.daoUi;

import com.thinkingdata.webui.entityUi.ExpectImage;
import org.springframework.stereotype.Repository;

/**
 * @author Fernando Zhu
 * @version 1.0
 * @date 2022/09/27 14:16
 */
@Repository
public interface ExpectImageDao {
    public Integer addImage(ExpectImage expectImage);

    public Integer updateImage(ExpectImage expectImage);

    public Integer deleteImage(Integer stepId);

    public Integer deleteImageByPageId(Integer pageId);

    public ExpectImage getImage(Integer stepId);

}
