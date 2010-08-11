package org.qrone;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import javax.imageio.ImageIO;

import com.swetake.util.Qrcode;

/**
 * QR コード画像を生成用ツール<BR>
 * <BR>
 * com.swetake.util.Qrcode を利用して実際に QR コード画像を生成して
 * 出力する為の利便ライブラリ。
 * 
 * @author J.Tabuchi
 * @since 2005/8/16
 * @version 1.0
 * @link QrONE Technology : http://www.qrone.org/
 */
public class QRcodeTools {
	/**
	 * エラー訂正レベル L : 訂正率　約７％
	 */
	public static final char ERROR_COLLECT_L = 'L';
	/**
	 * エラー訂正レベル M : 訂正率　約１５％　（標準）
	 */
	public static final char ERROR_COLLECT_M = 'M';
	/**
	 * エラー訂正レベル Q : 訂正率　約２５％
	 */
	public static final char ERROR_COLLECT_Q = 'Q';
	/**
	 * エラー訂正レベル H : 訂正率　約３０％
	 */
	public static final char ERROR_COLLECT_H = 'H';
	
	/**
	 * エンコード：数字のみ
	 */
	public static final char ENCODE_NUMBER              = 'N';
	/**
	 * エンコード：英数字のみ
	 */
	public static final char ENCODE_ALPHABET_AND_NUMBER = 'A';
	/**
	 * エンコード：その他、8bit
	 */
	public static final char ENCODE_8_BIT               = 'B';
	
	/**
	 * バージョン自動指定
	 */
	public static final int  VERSION_AUTO = 0;

	/**
	 * 与えられた文字列から QR コード画像を生成し指定ファイルに出力します。
	 * @param str　入力文字列
	 * @param errorcollect　エラー訂正レベル
	 * @param encode　QR 画像エンコード（含まれる文字種）
	 * @param version　QR バージョン（画像サイズ）
	 * @param zoom　拡大倍率
	 * @param format ファイルフォーマット
	 * @param out 出力ストリーム
	 * @throws IOException 出力エラー
	 */
	public static void streamQRcodeImage(String str, char errorcollect, 
			char encode,int version, int zoom, String format, OutputStream out)
	throws IOException{
		
		RenderedImage image = createQRcodeImage(
				str,errorcollect,encode,version,zoom);
		ImageIO.write(image,format,out);
	}
	
	/**
	 * 与えられた文字列から QR コード画像を生成し指定ファイルに出力します。
	 * @param str　入力文字列
	 * @param version　QR バージョン（画像サイズ）
	 * @param zoom　拡大倍率
	 * @param file 出力ファイル名
	 * @throws IOException 出力エラー
	 */
	public static void createQRcodeImageFile(String str, int version, int zoom,
			String file) throws IOException{
		RenderedImage image = createQRcodeImage(
				str.getBytes(),ERROR_COLLECT_M,ENCODE_8_BIT,version,zoom);
		
		File f = new File(file);
		String format = file.substring(file.lastIndexOf(".")+1);
		ImageIO.write(image,format,f);
	}
	
	/**
	 * 与えられた文字列から QR コード画像を生成し指定ファイルに出力します。
	 * @param str　入力文字列
	 * @param version　QR バージョン（画像サイズ）
	 * @param zoom　拡大倍率
	 * @param file 出力ファイル
	 * @param format 出力ファイルの拡張子
	 * @throws IOException 出力エラー
	 */
	public static void createQRcodeImageFile(String str, int version, int zoom,
			File file, String format) throws IOException{
		RenderedImage image = createQRcodeImage(
				str.getBytes(),ERROR_COLLECT_M,ENCODE_8_BIT,version,zoom);
		ImageIO.write(image,format,file);
	}
	
	/**
	 * 与えられた文字列から QR コード画像を生成し指定ファイルに出力します。
	 * @param str　入力文字列
	 * @param errorcollect　エラー訂正レベル
	 * @param encode　QR 画像エンコード（含まれる文字種）
	 * @param version　QR バージョン（画像サイズ）
	 * @param zoom　拡大倍率
	 * @param file 出力ファイル
	 * @param format 出力ファイルの拡張子
	 * @throws IOException 出力エラー
	 */
	public static void createQRcodeImageFile(String str,
			char errorcollect, char encode, int version, int zoom,
			File file, String format) throws IOException{
		RenderedImage image = createQRcodeImage(
				str.getBytes(),errorcollect,encode,version,zoom);
		ImageIO.write(image,format,file);
	}
	/**
	 * 与えられた文字列から QR コード画像を生成し指定ファイルに出力します。
	 * @param str　入力文字列
	 * @param charset　入力文字列の文字コード
	 * @param errorcollect　エラー訂正レベル
	 * @param encode　QR 画像エンコード（含まれる文字種）
	 * @param version　QR バージョン（画像サイズ）
	 * @param zoom　拡大倍率
	 * @param file 出力ファイル
	 * @param format 出力ファイルの拡張子
	 * @throws IOException 出力エラー
	 */
	public static void createQRcodeImageFile(String str, String charset,
			char errorcollect, char encode, int version, int zoom,
			File file, String format) throws IOException{
		RenderedImage image = createQRcodeImage(
				str.getBytes(charset),errorcollect,encode,version,zoom);
		ImageIO.write(image,format,file);
	}

	/**
	 * 与えられた文字列から QR コード画像を生成します
	 * @param str　入力文字列
	 * @param errorcollect　エラー訂正レベル
	 * @param encode　QR 画像エンコード（含まれる文字種）
	 * @param version　QR バージョン（画像サイズ）
	 * @param zoom　拡大倍率
	 * @return　出力画像
	 */
	public static RenderedImage createQRcodeImage(String str,
			char errorcollect, char encode, int version, int zoom){
		return createQRcodeImage(
				str.getBytes(),errorcollect,encode,version,zoom);
	}
	
	/**
	 * 与えられた文字列から QR コード画像を生成します
	 * @param str　入力文字列
	 * @param charset　入力文字列の文字コード
	 * @param errorcollect　エラー訂正レベル
	 * @param encode　QR 画像エンコード（含まれる文字種）
	 * @param version　QR バージョン（画像サイズ）
	 * @param zoom　拡大倍率
	 * @return　出力画像
	 * @throws UnsupportedEncodingException 文字コード不正
	 */
	public static RenderedImage createQRcodeImage(String str, String charset,
			char errorcollect, char encode, int version, int zoom)
	throws UnsupportedEncodingException{
		return createQRcodeImage(
				str.getBytes(charset),errorcollect,encode,version,zoom);
	}
	
	/**
	 * 与えられたバイト列から QR コード画像を生成します
	 * @param b　入力バイト列
	 * @param errorcollect　エラー訂正レベル
	 * @param encode　QR 画像エンコード（含まれる文字種）
	 * @param version　QR バージョン（画像サイズ）
	 * @param zoom　拡大倍率
	 * @return　出力画像
	 */
	public static RenderedImage createQRcodeImage(byte[] b,
			char errorcollect, char encode, int version, int zoom){
		Qrcode qrcode = new Qrcode();
		qrcode.setQrcodeEncodeMode(encode);
		qrcode.setQrcodeErrorCorrect(errorcollect);
		qrcode.setQrcodeVersion(version);
		boolean[][] r = qrcode.calQrcode(b);
		
		BufferedImage image = new BufferedImage(
				r.length*zoom,r.length*zoom,BufferedImage.TYPE_INT_RGB );
		
		WritableRaster raster = image.getRaster();
		int[] inputints = new int[r.length*r.length*3*zoom*zoom];
		for(int y=0;y<r.length;y++){
			for(int yc=0;yc<zoom;yc++){
				for(int x=0;x<r.length;x++){
					for(int xc=0;xc<zoom;xc++){
						int p = (((y*zoom)+yc)*r.length*zoom 
				          			+ (x*zoom)+xc)*3;
						if(r[x][y]){
							inputints[p++] = 0;
							inputints[p++] = 0;
							inputints[p++] = 0;
						}else{
							inputints[p++] = 255;
							inputints[p++] = 255;
							inputints[p++] = 255;
						}
					}
				}
			}
		}
		raster.setPixels(0,0,r.length*zoom,r.length*zoom,inputints);
		return image;
	}
	
	/**
	 * 動作デモ
	 * @param args
	 */
	public static void main(String[] args){
		if(args.length == 0){
			try { createQRcodeImageFile(
						"QRcodeTools by J.Tabuchi",7,2,"qrcode-tools.png");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		String str = args[0];
		char errorcollect = args[1].charAt(0);
		char encode = args[2].charAt(0);
		int version = Integer.parseInt(args[3]);
		int zoom = Integer.parseInt(args[4]);
		String format = args[5];
		try {
			streamQRcodeImage(str, errorcollect, encode, 
					version, zoom, format, System.out);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
