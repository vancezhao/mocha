package com.coral.vaadin.image;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

import javax.imageio.ImageIO;

import com.coral.foundation.security.model.BasicUser;
import com.vaadin.Application;
import com.vaadin.terminal.FileResource;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Embedded;

public abstract class AbstractUserIconHelper {
//	public static BasicUser rebuildUserIcon(BasicUser basicUser) {
//////		String user_photo_path = "photos";
////		String user_icon_path = "photos/icon/";
////		String user_defalut_photo_name = "defalut_user_photo.jpg";
////		String uesr_default_icon_name = "defalut_user_icon.jpg";
//		if (basicUser.getUserIcon() == null) {
//
//			BufferedImage originalImage;
//			try {
//				String userPhoto = basicUser.getUserPhoto();
//				originalImage = null;
//				if (!new File(userPhoto).exists()) {
//					basicUser.setUserIcon(SystemProperty.user_icon_path
//							+ SystemProperty.user_defalut_photo_name);
//				} else {
//					originalImage = ImageIO.read(new File(userPhoto));
//					int type = originalImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB
//							: originalImage.getType();
//					BufferedImage resizeImageJpg = resizeImage(originalImage,
//							type);
//					UUID userIconName = UUID.randomUUID();
//					File userIconFile = new File(SystemProperty.uesr_default_icon_name
//							+ userIconName + ".jpg");
//					ImageIO.write(resizeImageJpg, "jpg", userIconFile);
//					basicUser.setUserIcon(userIconFile.getPath());
//				}
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//		return basicUser;
//	}

	private static BufferedImage resizeImage(BufferedImage originalImage,
			int type) {
		int userIconHeight = 25;
		int userIconWidth = 25;
		BufferedImage resizedImage = new BufferedImage(userIconHeight,
				userIconWidth, type);
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(originalImage, 0, 0, userIconHeight, userIconWidth, null);
		g.dispose();
		return resizedImage;
	}
	
//	public static String getDefaultUserIcon(){
//		return SystemProperty.user_icon_path+SystemProperty.uesr_default_icon_name;
//	}
	
	public abstract String getDefaultUserIcon();

	public BasicUser rebuildUserIcon(BasicUser basicUser) {
		// TODO Auto-generated method stub
		return null;
	}
}
