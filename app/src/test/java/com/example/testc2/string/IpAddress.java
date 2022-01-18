package com.example.testc2.string;


import org.junit.Test;

/**
 * Given a string queryIP, return "IPv4" if IP is a valid IPv4 address, "IPv6" if IP is a valid IPv6 address or "Neither" if IP is not a correct IP of any type.
 *
 * A valid IPv4 address is an IP in the form "x1.x2.x3.x4" where 0 <= xi <= 255 and xi cannot contain leading zeros. For example, "192.168.1.1" and "192.168.1.0" are valid IPv4 addresses but "192.168.01.1", while "192.168.1.00" and "192.168@1.1" are invalid IPv4 addresses.
 *
 * A valid IPv6 address is an IP in the form "x1:x2:x3:x4:x5:x6:x7:x8" where:
 *
 * 1 <= xi.length <= 4
 * xi is a hexadecimal string which may contain digits, lower-case English letter ('a' to 'f') and upper-case English letters ('A' to 'F').
 * Leading zeros are allowed in xi.
 *
 *
 * For example,
 * "2001:0db8:85a3:0000:0000:8a2e:0370:7334"
 * "2001:db8:85a3:0:0:8A2E:0370:7334"
 * are valid IPv6 addresses,
 *
 * "2001:0db8:85a3::8A2E:037j:7334"
 * "02001:0db8:85a3:0000:0000:8a2e:0370:7334"
 * invalid IPv6 addresses.
 *
 *  
 *
 * Example 1:
 *
 * Input: queryIP = "172.16.254.1"
 * Output: "IPv4"
 * Explanation: This is a valid IPv4 address, return "IPv4".
 * Example 2:
 *
 * Input: queryIP = "2001:0db8:85a3:0:0:8A2E:0370:7334"
 * Output: "IPv6"
 * Explanation: This is a valid IPv6 address, return "IPv6".
 * Example 3:
 *
 * Input: queryIP = "256.256.256.256"
 * Output: "Neither"
 * Explanation: This is neither a IPv4 address nor a IPv6 address.
 *
 *

 https://leetcode-cn.com/problems/validate-ip-address
 *
 */
public class IpAddress {

    @Test
    public void test2(){
        String str = "172.16.254.1";
        String[] v4 = str.split("\\.");
        System.out.println("s:"+v4.length);
    }
    @Test
    public void test3(){
        String str = "a.b.c.d";
        String[] v4 = str.split("\\.");
        System.out.println("s:"+v4.length);
    }

    @Test
    public void test4(){
        String str = "2001:0db8:85a3:0:0:8A2E:0370:7334";
        System.out.println("contains:"+str.contains("."));
        String[] v4 = str.split(":");
        System.out.println("s:"+v4.length);
    }

    @Test
    public void test5(){
        String str = "2001:0db8:85a3:0:0:8A2E:0370:7334:";
        String[] v4 = str.split(":");
        System.out.println("s:"+v4.length);
    }

    @Test
    public void test6(){
        String str = "20EE:FGb8:85a3:0:0:8A2E:0370:7334";
        System.out.println(validIPAddress(str));
    }

    @Test
    public void test7(){
        String str = "192.0.0.1";
        System.out.println(validIPAddress(str));
    }

    @Test
    public void test1(){
        String str = "172.16.254.1";
        System.out.println(validIPAddress(str));
    }

    public String validIPAddress(String queryIP) {
        if(queryIP.contains(".")){  //   !!!!
            int f = queryIP.indexOf(".");
            if(f==0){
                return "Neither";
            }
            int l = queryIP.lastIndexOf(".");
            if(l==(queryIP.length()-1)){
                return "Neither";
            }
            String[] v4 = queryIP.split("\\.");   //   !!!!
            if(v4.length!=4){
                return "Neither";
            }
            for(int i=0;i<4;i++){
                if(!validV4(v4[i])){
                    return "Neither";
                }
            }
            return "IPv4";
        }
        if(queryIP.contains(":")){
            int f = queryIP.indexOf(":");
            if(f==0){
                return "Neither";
            }
            int l = queryIP.lastIndexOf(":");
            if(l==(queryIP.length()-1)){
                return "Neither";
            }
            String[] v6 = queryIP.split(":");
            if(v6.length!=8){
                return "Neither";
            }
            for(int i=0;i<8;i++){
                if(!validV6(v6[i])){
                    return "Neither";
                }
            }
            return "IPv6";
        }
        return "Neither";
    }


    private boolean validV4(String str){
        boolean starts0 = str.startsWith("0");
        int i = 0;
        try {
            i = Integer.parseInt(str);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return false;
        }
        if( (i==0) && (str.length()>1)){
            return false;
        }
        if( (i!=0) && starts0){
            return false;
        }
        if((i>=0)&&(i<=255)){
            return true;
        }
        return false;
    }

    private boolean validV6(String str){
        int s = str.length();
        if( (s>4) || (s==0) ){
            return false;
        }
        String copy = str.toLowerCase();
        for (int i=0;i<s;i++){
            char c = copy.charAt(i);
            System.out.println("c:"+c);
            boolean valid = false;
            if (( c>='a' ) && (c<='f')){
                valid = true;
            }
            if (( c>='0' ) && (c<='9')){
                valid = true;
            }
            if(!valid){
                return false;
            }
        }
        return true;
    }


}
