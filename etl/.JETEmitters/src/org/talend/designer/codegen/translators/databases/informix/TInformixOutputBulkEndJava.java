package org.talend.designer.codegen.translators.databases.informix;

import org.talend.core.model.process.INode;
import org.talend.designer.codegen.config.CodeGeneratorArgument;

public class TInformixOutputBulkEndJava
{
  protected static String nl;
  public static synchronized TInformixOutputBulkEndJava create(String lineSeparator)
  {
    nl = lineSeparator;
    TInformixOutputBulkEndJava result = new TInformixOutputBulkEndJava();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = "\tcsvWriter_";
  protected final String TEXT_2 = ".close();" + NL + "\tglobalMap.put(\"";
  protected final String TEXT_3 = "_NB_LINE\",nb_line_";
  protected final String TEXT_4 = ");";
  protected final String TEXT_5 = NL;

  public String generate(Object argument)
  {
    final StringBuffer stringBuffer = new StringBuffer();
    
	CodeGeneratorArgument codeGenArgument = (CodeGeneratorArgument) argument;
	INode node = (INode)codeGenArgument.getArgument();
	String cid = node.getUniqueName();

    stringBuffer.append(TEXT_1);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_2);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_3);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_4);
    stringBuffer.append(TEXT_5);
    return stringBuffer.toString();
  }
}
