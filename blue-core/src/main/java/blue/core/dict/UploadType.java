package blue.core.dict;

/**
 * 上传文件类型
 * 
 * @author zhengj
 * @since 1.0 2017年1月14日
 */
public final class UploadType extends Dictionary
{
	public static final UploadType UNKNOWN = new UploadType(0, "未知", Color.BLACK);
	public static final UploadType PIC = new UploadType(1, "图片", Color.BLUE);
	public static final UploadType VIDEO = new UploadType(2, "视频", Color.RED);
	public static final UploadType DOC = new UploadType(3, "文档", Color.MAROON);
	public static final UploadType ZIP = new UploadType(4, "压缩包", Color.PURPLE);

	private UploadType(int index, String name, Color color)
	{
		super(index, name, color);
	}
	
	/**
	 * 判断上传文件类型
	 * 
	 * @param mimeType HTTP MIME-TYPE
	 * @return 上传文件类型
	 */
	public static UploadType getType(String mimeType)
	{
		if (mimeType == null || mimeType.isEmpty())
			return UploadType.UNKNOWN;
		
		if (mimeType.startsWith("image"))
			return UploadType.PIC;
		else if (mimeType.startsWith("video"))
			return UploadType.VIDEO;
		else if (mimeType.equals("application/msword") || mimeType.equals("application/pdf")
				 || mimeType.equals("application/vnd.ms-excel") || mimeType.equals("application/x-xls")
				 || mimeType.equals("application/vnd.ms-powerpoint") || mimeType.equals("application/x-ppt"))
			return UploadType.DOC;
		else if (mimeType.endsWith(".rar") || mimeType.endsWith(".zip"))
			return UploadType.ZIP;
		else
			return UploadType.UNKNOWN;
	}
	
	/**
	 * 判断上传文件类型
	 * 
	 * @param mimeType HTTP MIME-TYPE
	 * @param fileName 文件名称
	 * @return 上传文件类型
	 */
	public static UploadType getType(String mimeType, String fileName)
	{
		UploadType type = getType(mimeType);
		if (fileName == null || fileName.isEmpty() || type != UploadType.UNKNOWN)
			return type;
		
		fileName = fileName.toLowerCase();
		if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") || fileName.endsWith(".png")
				|| fileName.endsWith(".bmp") || fileName.endsWith(".gif"))
			type = UploadType.PIC;
		else if (fileName.endsWith(".mp4") || fileName.endsWith(".mkv") || fileName.endsWith(".avi")
				|| fileName.endsWith(".mov") || fileName.endsWith(".rm") || fileName.endsWith(".rmvb"))
			type = UploadType.VIDEO;
		else if (fileName.endsWith(".doc") || fileName.endsWith(".docx") || fileName.endsWith(".pdf")
				|| fileName.endsWith(".xls") || fileName.endsWith(".xls")
				|| fileName.endsWith(".ppt") || fileName.endsWith(".pptx"))
			type = UploadType.DOC;
		else if (fileName.endsWith(".zip") || fileName.endsWith(".rar") || fileName.endsWith(".7z"))
			type = UploadType.ZIP;
		return type;
	}

}
