
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.String.valueOf;

public class Count {

    private static final Map<Character, Integer> symbol = new HashMap<Character, Integer>();
    static {
        symbol.put('-', 1);//判断优先级
        symbol.put('+', 1);
        symbol.put('*', 2);
        symbol.put('/', 2);
    }
                //中缀表达式转后缀表达式
    public static String math(String exp){
        List<String> queue = new ArrayList<String>();                                    //队列用来存储数字
        List<Character> stack = new ArrayList<Character>();                              //栈用来存储运算符
        char[] charArray = exp.trim().toCharArray();
        String sta = "*/+-";
        char ch = '&';
        int len = 0;
        for (int i = 0; i < charArray.length; i++) {                                    //循环

            ch = charArray[i];
            if(Character.isDigit(ch)) {                                          //变量为 数字
                len++;
            }else if(Character.isLetter(ch)) {                                  //变量为  字母
                len++;
            }else if(ch == '.'){                                              //变量为  .  会出现在小数里面
                len++;
            }else if(Character.isSpaceChar(ch)) {
                if(len > 0) {
                    queue.add(valueOf(Arrays.copyOfRange(charArray, i - len, i)));
                    len = 0;
                }
                continue;                                                                //如果空格出现，则跳出循环
            }else if(sta.indexOf(ch) != -1) {
                if(len > 0) {
                    queue.add(valueOf(Arrays.copyOfRange(charArray, i - len, i)));
                    len = 0;
                }
                if(ch == '(') {                                                            //如果是左括号
                    stack.add(ch);                                                        //将左括号 放入栈中
                    continue;                                                            //跳出本次循环  继续找下一个位置
                }
                if (!stack.isEmpty()) {                                                    //如果栈不为空，指向栈最后一个元素的下标
                    int size = stack.size() - 1;
                    boolean flag = false;
                    while (size >= 0 && ch == ')' && stack.get(size) != '(') {
                        queue.add(valueOf(stack.remove(size)));
                        size--;
                        flag = true;
                    }
                    while (size >= 0 && !flag && symbol.get(stack.get(size)) >= symbol.get(ch)) {
                        queue.add(valueOf(stack.remove(size)));
                        size--;
                    }
                }
                if(ch != ')') {
                    stack.add(ch);
                } else {
                    stack.remove(stack.size() - 1);
                }
            }
            if(i == charArray.length - 1) {                               //如果指向中缀表达式的最后一位
                if(len > 0) {
                    queue.add(valueOf(Arrays.copyOfRange(charArray, i - len+1, i+1)));
                }
                int size = stack.size() - 1;                              //size表示栈内最后一个元素下标
                while (size >= 0) {
                    queue.add(valueOf(stack.remove(size)));
                    size--;
                }
            }

        }
        return queue.stream().collect(Collectors.joining(","));
    }


    /**
     后缀表达式计算结果
     */
    public static String dealEquation(String equation){
        String [] array = equation.split(",");
        List<String> list = new ArrayList<String>();


        for (int i = 0; i < array.length; i++) {
            int size = list.size();
            switch (array[i]) {
                case "+":
                    double a = Double.parseDouble(list.remove(size-2))+ Double.parseDouble(list.remove(size-2));
                    list.add(valueOf(a));
                    break;
                case "-":
                    double b = Double.parseDouble(list.remove(size-2))- Double.parseDouble(list.remove(size-2));
                    list.add(valueOf(b));
                    break;
                case "*":
                    double c = Double.parseDouble(list.remove(size-2))* Double.parseDouble(list.remove(size-2));
                    list.add(valueOf(c));
                    break;
                case "/":
                    double d = Double.parseDouble(list.remove(size-2))/ Double.parseDouble(list.remove(size-2));
                    list.add(valueOf(d));
                    break;
                default:
                    list.add(array[i]);
                    break;
            }
        }

        return list.size() == 1 ? list.get(0) : "运算失败" ;                    //最终list中仅有一个结果，否则就是算错了
    }


    public static void Cal() throws IOException {
       System.out.println("输入出题数目：");
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();

        FileWriter fw=new FileWriter("result.txt");
        BufferedWriter b1=new BufferedWriter(fw);
        b1.write("2016012013");
        //随机产生四则运算
        while (n-- > 0) {
            Random ran1 = new Random();
            int length = 0;
            String[] array = new String[100];
            int r =ran1.nextInt(3)+3;           //产生三到五个随机数
            for (int i = 0; i <r; i++) {
                length++;
                array[i] = valueOf(ran1.nextInt(100));
            }

            char[] ch = {'+', '-', '*', '/'};          //符号数组
            for (int i = 0; i < r-1; i++) {
                int index = ran1.nextInt(ch.length);
                int head = (i + 1) * 2;
                int tail = length + 1;
                for (int j = tail; j >= head; j--) {
                    array[j] = array[j - 1];            //与前一个进行交换
                }
                array[head - 1] = valueOf(ch[index]);
                length++;
            }
            String p="";
            for(int i=0;i<length;i++){
                p+=array[i];
            }


            String a=math(p);
            String result=dealEquation(a);
            float rs1=Float.valueOf(result);           //判断是否为浮点型

            int rs2=(int)rs1;
            float rs3=rs1-(float) rs2;
            if(rs3>0.000||rs2<0){
                n++;                               //出去负数和小数
                continue;
            }


            System.out.print(p);
            System.out.println("="+rs2);



            b1.newLine();
            b1.write(p+"="+rs2);




        }
        b1.close();
    }
}
