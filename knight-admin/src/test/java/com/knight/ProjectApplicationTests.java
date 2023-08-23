package com.knight;

import cn.hutool.core.io.FileUtil;
import com.knight.entity.base.R;
import com.knight.storage.template.StorageTemplate;
import com.knight.storage.vo.response.OssUploadR;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
public class ProjectApplicationTests {

	@Resource
	private StorageTemplate storageTemplate;

	@Test
	void download() {
		String filePath = "D:\\1.txt";
		R<OssUploadR> uploadR = storageTemplate.upload(storageTemplate.filePathGenerate(FileUtil.getName(filePath)),
				FileUtil.getInputStream(filePath));
		assert uploadR.isSuccess();

		String destPath = "D:\\2.txt";
		storageTemplate.download(uploadR.getData().getObjectName(), destPath);
		assert FileUtil.exist(destPath);
	}

}
