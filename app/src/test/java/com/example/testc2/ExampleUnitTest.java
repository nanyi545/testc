package com.example.testc2;

import org.junit.Test;

import java.util.LinkedList;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }


    @Test
    public void call1(){
//        int[] arr1 = {1,2,3,4,5};
//        test1(arr1,1);

        String value =  "0.5";
        String[] values = value.split("\\.");
        System.out.print(values.length);

    }




    Node head;
    Node tail;
    Node pre=null;

    public void test1(int[] arr, int step){

        //
        for (int i=0;i<arr.length;i++){
            Node t = new Node(arr[i]);
            if(i==0){
                head = t;
            }
            if(i==arr.length-1){
                tail = t;
            }
            if(pre!=null){
                pre.next = t;
                t.pre = pre;
            }
            pre = t;
        }


        for (int i=0;i<step;i++) {
            shift1Step();
        }

        Node n = head;
        while (n!=null){
            System.out.print(n.value+"|");
            n = n.next;
        }

    }


    private void shift1Step(){

        Node t = head;
        Node secondLast = tail.pre;
        secondLast.next = null;
        tail.pre = null;

        tail = secondLast;

        head = tail;
        head.pre=null;
        head.next = t;

    }






    static class Node {

        int value;

        Node next;
        Node pre;


        public Node(int value) {
            this.value = value;
        }

    }


    void test1(){
        //  08-19 20:48:35.708 D/UT_LOG_TAG( 3794): Id:2201   reStr:{"tbdm":{"foreign":{"clickUrl":"aHR0cDovL2UuZHR2LmNuLm1pYW96aGVuLmNvbS9yL2s9NDA1MjkxMSZwPTJuS3lXJm5zPV9fSVBfXyZueD1fX0xBQjFfXyZzbj1fX1NOX18mbmk9X19JRVNJRF9fJm0xPV9fQU5EUk9JRElEMV9fJm0xYT1fX0FORFJPSURJRF9fJm00PV9fQUFJRF9fJm02PV9fTUFDMV9fJm02YT1fX01BQ19fJm5kPV9fRFJBX18mbm49X19BUFBfXyZuZz1fX0NUUkVGX18mbmM9X19WSURfXyZudD1fX1RJTUVfXyZyZm09X19SRk1fXyZ0ZHQ9X19URFRfXyZ0ZHI9X19URFJfXyZwcm89biZ2dj0xJnR2cm09X19UUkFOU0lEX18mdHI9X19SRVFVRVNUSURfXyZtNm89X19NNk9fXyZ0cnk9MSZtYWNyPV9fTUFDUl9fJmR4PV9fSVBEWF9fJmZpeD1fX0ZJWF9fJm56PV9fTEFCX18mYWRzPTEyJm09NDUmbTZiPV9fTUFDMl9fJm02Yz1fX01BQzNfXyZtNmQ9X19NQUM0X18mbnZuPV9fVk5BTUVfXyZ0cjI9X19SRVFVRVNUSUQyX18mdHIzPV9fUkVRVUVTVElEM19fJm5sPV9fU0NFTkVJRF9fJm89","exposeUrl":"aHR0cDovL2cuZHR2LmNuLm1pYW96aGVuLmNvbS94L2s9NDA1MjkxMSZwPTJuS3lXJm5zPV9fSVBfXyZueD1fX0xBQjFfXyZzbj1fX1NOX18mbmk9X19JRVNJRF9fJm0xPV9fQU5EUk9JRElEMV9fJm0xYT1fX0FORFJPSURJRF9fJm00PV9fQUFJRF9fJm02PV9fTUFDMV9fJm02YT1fX01BQ19fJnJ0PTImbmQ9X19EUkFfXyZubj1fX0FQUF9fJm5nPV9fQ1RSRUZfXyZuYz1fX1ZJRF9fJm50PV9fVElNRV9fJnJmbT1fX1JGTV9fJnRkdD1fX1REVF9fJnRkcj1fX1REUl9fJnBybz1uJnZ2PTEmdHZybT1fX1RSQU5TSURfXyZ0cj1fX1JFUVVFU1RJRF9fJm02bz1fX002T19fJnRyeT0xJm1hY3I9X19NQUNSX18mZHg9X19JUERYX18mZml4PV9fRklYX18mbno9X19MQUJfXyZhZHM9MTImbT00NSZtNmI9X19NQUMyX18mbTZjPV9fTUFDM19fJm02ZD1fX01BQzRfXyZudm49X19WTkFNRV9fJnRyMj1fX1JFUVVFU1RJRDJfXyZ0cjM9X19SRVFVRVNUSUQzX18mbmw9X19TQ0VORUlEX18mbz0="}},"utReport":{"reportList":[{"args":{"zp_ad_strategy_group_id":4,"zp_ad_record_id":153791835,"zp_ad_business_type":2,"zp_content_id":5687,"zp_ad_subject_id":2581,"zp_ad_adgroup_id":102136,"zp_ad_plan_id":102053,"zpAdId":"0_19870102","zp_ad_scene_res_id":300001003,"zp_ad_activity_id":1002039,"zp_ad_scene_id":300,"zp_ad_device_hash":2056787173},"callBack":true,"clickId":"Button_Shoppingscreen_BannerPit","exposureId":"Expose_Shoppingscreen_BannerPit","page":"Page_ShoppingScreen"}]}}
    }


}