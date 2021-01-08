package org.opensrp.common.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.imgscalr.Scalr;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageUpload {
	
	public String upload(MultipartFile file, String dir, boolean isthumbnail, Integer width) {
		try {
			byte[] bytes;
			if (isthumbnail) {
				bytes = createThumbnail(file, width).toByteArray();
			} else {
				bytes = file.getBytes();
			}
			String uuid = UUID.randomUUID().toString();
			String name = uuid + getFileExtension(file.getOriginalFilename());
			Path path = Paths.get(dir + name);
			Files.write(path, bytes);
			return name;
		}
		catch (IOException e) {
			return "0";
		}
		
	}
	
	private String getFileExtension(String name) {
		int lastIndexOf = name.lastIndexOf(".");
		if (lastIndexOf == -1) {
			return "";
		}
		return name.substring(lastIndexOf);
	}
	
	private ByteArrayOutputStream createThumbnail(MultipartFile orginalFile, Integer width) throws IOException {
		ByteArrayOutputStream thumbOutput = new ByteArrayOutputStream();
		BufferedImage thumbImg = null;
		BufferedImage img = ImageIO.read(orginalFile.getInputStream());
		thumbImg = Scalr.resize(img, Scalr.Method.AUTOMATIC, Scalr.Mode.AUTOMATIC, width, Scalr.OP_ANTIALIAS);
		ImageIO.write(thumbImg, orginalFile.getContentType().split("/")[1], thumbOutput);
		return thumbOutput;
	}
}
