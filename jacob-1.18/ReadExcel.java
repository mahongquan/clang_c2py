import com.jacob.com.*;
import com.jacob.activeX.*;

class ReadExcel
{
  private static ActiveXComponent xl;
  private static Dispatch workbooks = null;
  private static Dispatch workbook = null;
  private static Dispatch sheet = null;
  private static String filename =null;
  private static boolean readonly = false;
 
  public static void main(String[] args)
  {
     String file = "D:\\clang1\\jacob-1.18\\Book1.xlsx";
     OpenExcel(file,false);//false为不显示打开Excel
//     SetValue("A1","Value","2");
     System.out.println(GetValue("G10"));
     CloseExcel(false);
  }
 
  //打开Excel文档
  private static void OpenExcel(String file,boolean f)
  {
    try
    {
        filename = file;
        xl = new ActiveXComponent("Excel.Application");
        xl.setProperty("Visible", new Variant(f));
        workbooks = xl.getProperty("Workbooks").toDispatch();
         workbook = Dispatch.invoke(workbooks,
                "Open",
                Dispatch.Method,
                                    new Object[]{filename,
                                    new Variant(false),
                                    new Variant(readonly)},//是否以只读方式打开
                                    new int[1] ).toDispatch();
    }catch(Exception e)
    {e.printStackTrace();}
  }
 
  //关闭Excel文档
  private static void CloseExcel(boolean f)
  {
   try
   {  
     Dispatch.call(workbook,"Save");
         Dispatch.call(workbook, "Close", new Variant(f));
      } catch (Exception e) {
         e.printStackTrace();
     } finally {
     xl.invoke("Quit", new Variant[] {});
      }
  }
 
  //写入值
  private static void SetValue(String position,String type,String value)
  {
//     sheet = Dispatch.get(workbook,"ActiveSheet").toDispatch();
      Dispatch  sheets = Dispatch.get(workbook,"Sheets").toDispatch();
//以编号读写sheet
      sheet = Dispatch.invoke(sheets, "Item", Dispatch.Get,  
      new Object[] { new String("1") }, new int[1]).toDispatch();
     Dispatch cell = Dispatch.invoke(sheet, "Range",
             Dispatch.Get,
                                    new Object[] {position},
                                    new int[1]).toDispatch();
      Dispatch.put(cell, type, value);
  }

  //读取值 
  private static String GetValue(String position)
  {
     
//      sheet = Dispatch.get(workbook,"ActiveSheet").toDispatch();
      Dispatch  sheets = Dispatch.get(workbook,"Sheets").toDispatch();
//以名称读写sheet
      sheet = Dispatch.invoke(sheets, "Item", Dispatch.Get,  
      new Object[] { new String("基础设施情况") }, new int[1]).toDispatch();
      Dispatch cell = Dispatch.invoke(sheet,"Range",Dispatch.Get,new Object[] {position},new int[1]).toDispatch();
  String value = Dispatch.get(cell,"Value").toString();
 
  return value;
  }
}