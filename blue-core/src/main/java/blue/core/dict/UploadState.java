package blue.core.dict;

/**
 * 上传文件状态
 * 
 * @author zhengj
 * @since 1.0 2017年1月14日
 */
public final class UploadState extends Dictionary
{
	public static final UploadState NOT_UPLOAD = new UploadState(0, "未上传", Color.DANGER);
	public static final UploadState UPLOADED = new UploadState(1, "已上传", Color.PRIMARY);

	private UploadState(int index, String name, Color color)
	{
		super(index, name, color);
	}

}
