import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

public class JacobDemo
{
	private static ActiveXComponent activeX;//ActiveX�ؼ�����
	private static Dispatch dispath;//MS����ĵ��ȶ���
		
	static
	{
		//����ActiveX�ؼ�����
		//����ʹ��CLSID,Ҳ����ʹ��ProgID
		activeX = new ActiveXComponent("CLSID:A71B53F3-562C-451E-9F92-DCF5B8852B6E");
		//��õ��ȶ���
		dispath = activeX.getObject();
	}
	
	/**
	 * ��½110����������
	 * @param ip ip��ַ
	 * @param port �˿ں�
	 * @param clientType �ͻ������
	 * @param loginName ��½�������ƣ���ʽΪ��(���������ƣ�����Ա<span class="wp_keywordlink"><a target="_blank" href="http://www.xuebuyuan.com/" title="����">����</a></span>������Ա����)
	 * @return -1����½ʧ�ܣ�0 ��ʼ��½��1,2 �ѵ�½ �� 3,4 ��½��
	 */
	public int loginSvr(String ip,String port,int clientType,String loginName)
	{	
		//��½�󷵻�ֵ(Ĭ��Ϊ-1)
		int result=-1;
	    if(!ip.isEmpty()&&!port.isEmpty())
	    {	    	
	    	//���Ե�½5�Σ�ÿ���1��һ��
	    	for(int i=0;i<5;i++)
	    	{
	    		System.out.println("���Ե�"+(i+1)+"�ε�½.");
	    		//���ÿؼ��ĵ�½����
	    		Variant loginVar =Dispatch.call(dispath, "Login", ip,port,clientType,loginName);
			    //�������ֵ
			    result= loginVar.getInt();
			    //�����½�ɹ�������ѭ��
			    if(result==1||result==2)
			    {
			    	break;
			    }
			    else
			    {
			    	//�߳�����1��
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
		int result = demo.loginSvr("192.168.1.174", "6986", 0x1000000, "����������ȫ��������:mingdao:mingdao");
		if(result==1||result==2)
		{
			System.out.println("��½�ɹ�!");
		}
	}
}
