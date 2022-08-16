package io.silvicky.String;

public class HexParser {
    public static long parseHex(String s) throws ExpressionErr {
        char c;
        long ans=0;
        if(s.length()>16)throw new ExpressionErr("Too large!");
        for(int i=0;i<s.length();i++)
        {
            ans<<=4;
            c=s.charAt(i);
            if(c>='0'&&c<='9')ans+=c-'0';
            else if(c>='a'&&c<='f')ans+=c-'a'+10;
            else if(c>='A'&&c<='F')ans+=c-'A'+10;
            else throw new ExpressionErr("Invalid HEX value!");
        }
        return ans;
    }
}
