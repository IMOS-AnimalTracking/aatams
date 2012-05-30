package org.talend.designer.codegen.translators.databases.mysql;

import org.talend.core.model.process.INode;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.IMetadataColumn;
import org.talend.core.model.process.ElementParameterParser;
import org.talend.core.model.process.IConnection;
import org.talend.core.model.process.IConnectionCategory;
import org.talend.designer.codegen.config.CodeGeneratorArgument;
import org.talend.core.model.metadata.types.JavaTypesManager;
import org.talend.core.model.metadata.types.JavaType;
import java.util.List;

public class TMysqlOutputBulkMainJava
{
  protected static String nl;
  public static synchronized TMysqlOutputBulkMainJava create(String lineSeparator)
  {
    nl = lineSeparator;
    TMysqlOutputBulkMainJava result = new TMysqlOutputBulkMainJava();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = " " + NL + "            \t\t\tStringBuilder sb_";
  protected final String TEXT_2 = " = new StringBuilder();" + NL + "            \t\t\tString column_value_";
  protected final String TEXT_3 = " = \"\"; \t" + NL + "                    \t";
  protected final String TEXT_4 = NL + "                \t\t\tsb_";
  protected final String TEXT_5 = ".append(textEnclosure_";
  protected final String TEXT_6 = ");" + NL + "                \t\t\t";
  protected final String TEXT_7 = NL + "                \t\t\t\t\tcolumn_value_";
  protected final String TEXT_8 = " = String.valueOf(" + NL + "                \t\t\t\t\t\t\ttrue == ";
  protected final String TEXT_9 = ".";
  protected final String TEXT_10 = " ?\"1\":\"0\"" + NL + "                \t\t\t\t\t\t\t);" + NL + "                \t\t\t\t";
  protected final String TEXT_11 = NL + "                \t\t\t    \tcolumn_value_";
  protected final String TEXT_12 = " = String.valueOf(";
  protected final String TEXT_13 = ".";
  protected final String TEXT_14 = ");" + NL + "                \t\t\t    ";
  protected final String TEXT_15 = NL + "                \t\t\t    if(";
  protected final String TEXT_16 = ".";
  protected final String TEXT_17 = " != null){" + NL + "                \t\t\t        ";
  protected final String TEXT_18 = NL + "                \t\t\t            column_value_";
  protected final String TEXT_19 = " = ";
  protected final String TEXT_20 = ".";
  protected final String TEXT_21 = ".replaceAll(\"\\\\\\\\\",\"\\\\\\\\\\\\\\\\\");" + NL + "                \t\t\t            if(!\"\\\\\".equals(textEnclosure_";
  protected final String TEXT_22 = "+\"\") || !\"\\\\\".equals(escapeChar_";
  protected final String TEXT_23 = "+\"\")){" + NL + "                \t\t\t            \tcolumn_value_";
  protected final String TEXT_24 = " = column_value_";
  protected final String TEXT_25 = ".replaceAll(\"\"+textEnclosure_";
  protected final String TEXT_26 = ",\"\"+textEnclosure_";
  protected final String TEXT_27 = "+textEnclosure_";
  protected final String TEXT_28 = ");" + NL + "                \t\t\t            }" + NL + "                \t\t\t            ";
  protected final String TEXT_29 = NL + "                \t\t\t            column_value_";
  protected final String TEXT_30 = " = FormatterUtils.format_Date(";
  protected final String TEXT_31 = ".";
  protected final String TEXT_32 = ", ";
  protected final String TEXT_33 = ");" + NL + "                \t\t\t            ";
  protected final String TEXT_34 = NL + "                \t\t\t            column_value_";
  protected final String TEXT_35 = " = java.nio.charset.Charset.forName(";
  protected final String TEXT_36 = ").decode(java.nio.ByteBuffer.wrap(";
  protected final String TEXT_37 = ".";
  protected final String TEXT_38 = ")).toString();" + NL + "                \t\t\t            ";
  protected final String TEXT_39 = NL + "                \t\t\t\t\t\tcolumn_value_";
  protected final String TEXT_40 = " = String.valueOf(" + NL + "                \t\t\t\t\t\t\ttrue == ";
  protected final String TEXT_41 = ".";
  protected final String TEXT_42 = " ?\"1\":\"0\"" + NL + "                \t\t\t\t\t\t\t);" + NL + "                \t\t\t\t\t";
  protected final String TEXT_43 = NL + "                \t\t\t            column_value_";
  protected final String TEXT_44 = " = String.valueOf(";
  protected final String TEXT_45 = ".";
  protected final String TEXT_46 = ");" + NL + "                \t\t\t            ";
  protected final String TEXT_47 = "                \t\t\t    " + NL + "                \t\t\t    }else{" + NL + "                \t\t\t    \tcolumn_value_";
  protected final String TEXT_48 = " = \"\\\\N\";" + NL + "                \t\t\t    }" + NL + "                \t\t ";
  protected final String TEXT_49 = NL + "                \t\tsb_";
  protected final String TEXT_50 = ".append(column_value_";
  protected final String TEXT_51 = ");" + NL + "                \t\tcolumn_value_";
  protected final String TEXT_52 = "=\"\\\\N\";" + NL + "\t\t\t\t\t\tsb_";
  protected final String TEXT_53 = ".append(textEnclosure_";
  protected final String TEXT_54 = ");" + NL + "\t\t\t\t\t\t";
  protected final String TEXT_55 = NL + "\t\t\t\t\t\t\t\tsb_";
  protected final String TEXT_56 = ".append(fieldSeparator_";
  protected final String TEXT_57 = ");" + NL + "\t\t\t\t\t\t";
  protected final String TEXT_58 = NL + "\t\t\t\t\t\tsb_";
  protected final String TEXT_59 = ".append(rowSeparator_";
  protected final String TEXT_60 = ");" + NL + "\t            \t\t\t";
  protected final String TEXT_61 = NL + "\t            \t\t\t\tdiskSpace_";
  protected final String TEXT_62 = " = file_";
  protected final String TEXT_63 = ".getFreeSpace();" + NL + "\t            \t\t\t\tif(diskSpace_";
  protected final String TEXT_64 = " <= 0){" + NL + "\t\t\t\t\t\t\t\t\tthrow new java.io.IOException(\"The disk space is not enough,please check it!\");" + NL + "\t\t\t\t\t\t\t\t}" + NL + "\t            \t\t\t";
  protected final String TEXT_65 = NL + "            \t\t\t    out_";
  protected final String TEXT_66 = ".write(sb_";
  protected final String TEXT_67 = ".toString());\t" + NL + "\t            \t\t\t";
  protected final String TEXT_68 = NL + "        \t\t\t    \t\tout_";
  protected final String TEXT_69 = ".flush();" + NL + "\t            \t\t\t";
  protected final String TEXT_70 = NL + "            \t\t\t    ";
  protected final String TEXT_71 = NL + "        \t\t                if(nb_line_";
  protected final String TEXT_72 = "%";
  protected final String TEXT_73 = " == 0) {            \t\t\t    " + NL + "            \t\t\t    \t\tout_";
  protected final String TEXT_74 = ".flush();" + NL + "            \t\t\t    \t}\t " + NL + "            \t\t\t    ";
  protected final String TEXT_75 = NL + "            \t\t\t\tnb_line_";
  protected final String TEXT_76 = "++;" + NL + "            \t\t\t";
  protected final String TEXT_77 = NL;

  public String generate(Object argument)
  {
    final StringBuffer stringBuffer = new StringBuffer();
    
CodeGeneratorArgument codeGenArgument = (CodeGeneratorArgument) argument;
INode node = (INode)codeGenArgument.getArgument();
String cid = node.getUniqueName();
List<IMetadataTable> metadatas = node.getMetadataList();
if ((metadatas!=null)&&(metadatas.size()>0)) {
    IMetadataTable metadata = metadatas.get(0);
    if (metadata!=null) {
    
        boolean checkDiskSpace = ("true").equals(ElementParameterParser.getValue(node,"__CHECK_DISK_SPACE__"));
    	boolean flushOnRow = ("true").equals(ElementParameterParser.getValue(node, "__FLUSHONROW__"));
    	String flushMod = ElementParameterParser.getValue(node, "__FLUSHONROW_NUM__");
    	
        String encoding = ElementParameterParser.getValue(node,"__ENCODING__");    	
    	
    	List< ? extends IConnection> conns = node.getIncomingConnections();
        	if(conns!=null){
        		if (conns.size()>0){
        		    IConnection conn =conns.get(0);
            		if (conn.getLineStyle().hasConnectionCategory(IConnectionCategory.DATA)) {
            			List<IMetadataColumn> columns = metadata.getListColumns();
                		int sizeColumns = columns.size();
            			
    stringBuffer.append(TEXT_1);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_2);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_3);
    
            			for (int i = 0; i < sizeColumns; i++) {
                			IMetadataColumn column = columns.get(i);
                			JavaType javaType = JavaTypesManager.getJavaTypeFromId(column.getTalendType());
                			String pattern = column.getPattern() == null || column.getPattern().trim().length() == 0 ? null : column.getPattern();
                			
    stringBuffer.append(TEXT_4);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_5);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_6);
    
                			if(JavaTypesManager.isJavaPrimitiveType( column.getTalendType(), column.isNullable())){
                			
                				if(javaType == JavaTypesManager.BOOLEAN ){
                				
    stringBuffer.append(TEXT_7);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_8);
    stringBuffer.append(conn.getName() );
    stringBuffer.append(TEXT_9);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_10);
    
                				} else {
                			    
    stringBuffer.append(TEXT_11);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_12);
    stringBuffer.append(conn.getName() );
    stringBuffer.append(TEXT_13);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_14);
    
                			    }
                			}else {
                			    
    stringBuffer.append(TEXT_15);
    stringBuffer.append(conn.getName() );
    stringBuffer.append(TEXT_16);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_17);
    					
                			        if(javaType == JavaTypesManager.STRING ){
                			            
    stringBuffer.append(TEXT_18);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_19);
    stringBuffer.append(conn.getName() );
    stringBuffer.append(TEXT_20);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_21);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_22);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_23);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_24);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_25);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_26);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_27);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_28);
    
                			        }else if(javaType == JavaTypesManager.DATE && pattern != null){
                			            
    stringBuffer.append(TEXT_29);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_30);
    stringBuffer.append(conn.getName() );
    stringBuffer.append(TEXT_31);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_32);
    stringBuffer.append( pattern );
    stringBuffer.append(TEXT_33);
    
                			        }else if(javaType == JavaTypesManager.BYTE_ARRAY){
                			            
    stringBuffer.append(TEXT_34);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_35);
    stringBuffer.append(encoding );
    stringBuffer.append(TEXT_36);
    stringBuffer.append(conn.getName() );
    stringBuffer.append(TEXT_37);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_38);
    
                			        }else if(javaType == JavaTypesManager.BOOLEAN ){
                					
    stringBuffer.append(TEXT_39);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_40);
    stringBuffer.append(conn.getName() );
    stringBuffer.append(TEXT_41);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_42);
    
                					}	             			        
                			        else{
                			            
    stringBuffer.append(TEXT_43);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_44);
    stringBuffer.append(conn.getName() );
    stringBuffer.append(TEXT_45);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_46);
    
                			        }
                			        
    stringBuffer.append(TEXT_47);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_48);
     
                			}
						
    stringBuffer.append(TEXT_49);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_50);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_51);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_52);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_53);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_54);
    
							if(i<sizeColumns-1){
    stringBuffer.append(TEXT_55);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_56);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_57);
    
							}
            			} 
						
    stringBuffer.append(TEXT_58);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_59);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_60);
    if(checkDiskSpace){
    stringBuffer.append(TEXT_61);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_62);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_63);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_64);
    }
    stringBuffer.append(TEXT_65);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_66);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_67);
    if(checkDiskSpace){
    stringBuffer.append(TEXT_68);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_69);
    }
    stringBuffer.append(TEXT_70);
     if(flushOnRow) { 
    stringBuffer.append(TEXT_71);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_72);
    stringBuffer.append(flushMod );
    stringBuffer.append(TEXT_73);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_74);
    }
    stringBuffer.append(TEXT_75);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_76);
       		
            		}
        		
        		}
        	}
    }
}

    stringBuffer.append(TEXT_77);
    return stringBuffer.toString();
  }
}
