package com.knight.storage.vo.response;

import lombok.Data;

/**
 * oss返回值
 *
 * @author knight
 * @since 2023/01/15
 */
@Data
public class OssUploadR {

    /**
     * etag
     */
    private String etag;

    /**
     * 版本标识
     */
    private String versionId;

    /**
     * 桶
     */
    private String bucket;

    /**
     * 对象
     */
    private String objectName;

}
