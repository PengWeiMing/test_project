package com.pwm.leetCode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class LeetCodeDemo {

    /**
     * 1.求数组的动态和(No.1480)
     * 计算公式：runningSum[i]=sum(nums[0]...nums[i])
     **/

    public static int[] runningSum(int[] nums){
        for (int i = 1; i < nums.length; i++) {
            nums[i]+=nums[i-1];
        }
        return nums;
    }


    /**
     * 2.判断一个字符串是否包含另一个字符串的字符元素(No.383)
     **/

    public static boolean canConstruct(String ransomNote,String magazine){
        //判断是否被包含的字符串,从字符串转为字符数组,再从字符数组转为集合
        List<Character> ransomNoteList=new String(ransomNote).chars().mapToObj(c->(char)c).collect(Collectors.toList());

        //需要判断是否能够包含字符串元素的字符串,从字符串转为字符数组,再从字符数组转为集合
        List<Character> magazineList=new String(magazine).chars().mapToObj(c->(char)c).collect(Collectors.toList());

        //循环被包含的集合元素
        for(Character c:ransomNoteList){
            if(!magazineList.remove(c)){
               //如果是false代表该元素无法移除,说明有的元素不在里面,也代表无法满足条件
                return false;
            }
        }
        return true;
    }

    /**
     * 3.判断整数N(No.412)
     * 如果是3的倍数值为 Fizz
     * 如果是5的倍数值为 Buzz
     * 如果同时时3和5的倍数值为 FizzBuzz
     **/

    public static List<String> fizzBuzz(int n){
        List<String> list=new ArrayList<>();
        for(int i=1;i<=n;i++){
            if(i%3==0 && i%5==0){
                list.add(i-1,"FizzBuzz");
                continue;
            }
            if(i%3==0){
                list.add(i-1,"Fizz");
                continue;
            }
            if(i%5==0){
                list.add(i-1,"Buzz");
                continue;
            }
            list.add(i-1,String.valueOf(i));
        }
        return list;
    }


    /**
     * 4.取一个链表的中间节点(No.876)
     * 如果是偶数，则取右侧的节点
     **/
    public static class ListNode{
        int val;
        ListNode next;
        ListNode(){};
        ListNode(int val){
            this.val=val;
        }
        ListNode(int val,ListNode next){
            this.val=val;
            this.next=next;
        }
    }

    /**
     * 4.
     * 解法1 链表数组法
     * 数字越大效率越低
     **/
    public static ListNode middleNode1(ListNode head){
        int t=0;
        ListNode[] A=new ListNode[100];
        while (head!=null){
            A[t++]=head;
            head=head.next;
        }
        return A[t/2];
    }

    /**
     * 4.
     * 解法2 快慢指针法
     * 空间复杂度O(1)
     **/
    public static ListNode middleNode2(ListNode head){
        ListNode slow=head;
        ListNode fast=head;
        while (fast!=null && fast.next!=null){
            slow=slow.next;
            fast=fast.next.next;
        }
        return slow;
    }

    /**
     * 5.取一个非负整数到0的步数(No.1342)
     * 如果是偶数除以2，奇数减1，一直到0
     **/
    public static int numberOfSteps(int num){
        int steps=0;
        while(num>0){
            if(num%2==0){
                num=num/2;
            }else{
                num=num-1;
            }
            steps++;
        }
        return steps;
    }


    /**
     * 6.求一个二维数组里数字总和最大的数(No.1672)
     **/
    public static int maximumWealth(int[][] accounts){
        //初始化一个一维数组，长度是目标二位数组的长度
        int[] sum = new int[accounts.length];
        for (int i=0;i<accounts.length;i++){
            //循环二位数组的子数组并求和,赋值给一维数组
            sum[i]=Arrays.stream(accounts[i]).sum();
        }
        //返回一维数组的最大数并转型整型
        return Arrays.stream(sum).max().getAsInt();
    }


    /**
     * 7.删除有序数组中的重复项(No.26)
     * 在原数组中处理，位移的元素通过占位标记
     **/
    public static int removeDuplicates(int[] nums) {
        //双层FOR循环比较当前数组的元素和下一个元素是否相同
        for(int i=0;i<nums.length;i++){
            for(int j=i+1;j<nums.length;j++){
                if(nums[i]==nums[j]){
                    //如果相同则修改为最大值，方便后面的排序
                    nums[i]=99999;
                }
            }
        }
        //修改后的数组进行正序排序
        Arrays.sort(nums);

        //通过流排除掉最大值的元素，求得非重复元素的数组长度值
        return Arrays.stream(nums).filter(num->num!=99999).toArray().length;
    }

    public static void main(String[] args) {
        //1.求数组的动态和
       /* int[] nums={1,2,3,4};
        System.out.println(Arrays.toString(runningSum(nums)));*/


        //2.判断两个字符串是否包含元素
        /*String ransomNote="a";
        String magazine="b";
        System.out.println(canConstruct(ransomNote,magazine));*/

        //3.判断整数N
        /*List<String> list=fizzBuzz(15);
        System.out.println(list);*/

        //4.取一个链表的中间节点
        /*ListNode head=new ListNode(1);
        ListNode l1=new ListNode(2);
        ListNode l2=new ListNode(3);
        ListNode l3=new ListNode(4);
        ListNode l4=new ListNode(5);
        ListNode l5=new ListNode(6);
        head.next=l1;
        l1.next=l2;
        l2.next=l3;
        l3.next=l4;
        l4.next=l5;

        ListNode ans = middleNode2(head);
        System.out.println(ans.val);
        System.out.println(ans.next.val);
        System.out.println(ans.next.next.val);
        System.out.println(ans.next.next.next);*/

        //5.取一个非负整数到0的步数
        /*int steps = numberOfSteps(12345);
        System.out.println(steps);*/

        //6.求一个二维数组里数字总和最大的数
        /*int[][] accounts={{1,2,3},{2,3,4}};
        System.out.println(maximumWealth(accounts));*/

        //7.删除有序数组中的重复项
        /*int[] nums={0,0,1,1,1,2,2,3,3,4};
        System.out.println(removeDuplicates(nums));*/
    }
}
