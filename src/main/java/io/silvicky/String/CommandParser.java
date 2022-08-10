package io.silvicky.String;

import java.util.ArrayList;
import java.util.List;

public class CommandParser {
    public static List<String> parseStr(String s) throws ExpressionErr {
        List<String> ans=new ArrayList<>();
        String cur="";
        boolean isQuote=false;
        for(int i=0;i<s.length();i++)
        {
            if(s.charAt(i)=='\"')
            {
                if(isQuote)
                {
                    ans.add(cur);
                    cur="";
                    isQuote=false;
                    if(i+1<s.length()&&s.charAt(i+1)!=' ')throw new ExpressionErr("Parts must be separated with spaces.");
                    i++;
                }
                else isQuote=true;

            }
            else if(s.charAt(i)=='\\')
            {
                if(i+1==s.length())cur+='\\';
                else
                {
                    switch(s.charAt(i+1))
                    {
                        case 'n':
                            cur+='\n';
                            i++;
                            break;
                        case 't':
                            cur+='\t';
                            i++;
                            break;
                        case 'b':
                            cur+='\b';
                            i++;
                            break;
                        case 'r':
                            cur+='\r';
                            i++;
                            break;
                        case 'f':
                            cur+='\f';
                            i++;
                            break;
                        case '\\':
                            cur+='\\';
                            i++;
                            break;
                        case '\"':
                            cur+='\"';
                            i++;
                            break;
                        case '\'':
                            cur+='\'';
                            i++;
                            break;
                        default:
                            cur+='\\';
                    }
                }
            }
            else if(s.charAt(i)==' ')
            {
                if(isQuote)cur+=' ';
                else {
                    if(cur.length()!=0)ans.add(cur);
                    cur="";
                }
            }
            else
            {
                cur+=s.charAt(i);
            }
        }
        if(isQuote)throw new ExpressionErr("Incomplete quotes!");
        if(cur.length()!=0)ans.add(cur);
        return ans;
    }
}
