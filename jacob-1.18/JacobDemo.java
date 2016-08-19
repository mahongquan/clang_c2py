import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

public class JacobDemo
{
	private static ActiveXComponent activeX;//ActiveX控件对象
	private static Dispatch dispath;//MS级别的调度对象
		
	static
	{
		//创建ActiveX控件对象
		//可以使用CLSID,也可以使用ProgID
		activeX = new ActiveXComponent("CLSID:A71B53F3-562C-451E-9F92-DCF5B8852B6E");
		//获得调度对象
		dispath = activeX.getObject();
	}
	
	/**
	 * 登陆110报警服务器
	 * @param ip ip地址
	 * @param port 端口号
	 * @param clientType 客户端类别
	 * @param loginName 登陆中心名称，格式为：(分中心名称：操作员<span class="wp_keywordlink"><a target="_blank" href="http://www.xuebuyuan.com/" title="代码">代码</a></span>：操作员名称)
	 * @return -1：登陆失败；0 开始登陆；1,2 已登陆 ； 3,4 登陆中
	 */
	public int loginSvr(String ip,String port,int clientType,String loginName)
	{	
		//登陆后返回值(默认为-1)
		int result=-1;
	    if(!ip.isEmpty()&&!port.isEmpty())
	    {	    	
	    	//尝试登陆5次，每间隔1秒一次
	    	for(int i=0;i<5;i++)
	    	{
	    		System.out.println("尝试第"+(i+1)+"次登陆.");
	    		//调用控件的登陆函数
	    		Variant loginVar =Dispatch.call(dispath, "Login", ip,port,clientType,loginName);
			    //给结果赋值
			    result= loginVar.getInt();
			    //如果登陆成功，跳出循环
			    if(result==1||result==2)
			    {
			    	break;
			    }
			    else
			    {
			    	//线程休眠1秒
			    	try
					{
						Thread.sleep(1*1000);
					} catch (InterruptedException e)
					{
						e.printStackTrace();
					}
			    }
	    	}
	    }	    
	    return result;
	}
	
	public static void main(String[] args)
	{
		JacobDemo demo = new JacobDemo();
		int result = demo.loginSvr("192.168.1.174", "6986", 0x1000000, "江苏明道保全报警中心:mingdao:mingdao");
		if(result==1||result==2)
		{
			System.out.println("登陆成功!");
		}
	}
}
