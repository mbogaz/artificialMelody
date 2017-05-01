import java.util.ArrayList;

public class Test {

    public static void main(String[] args) throws Exception {

        String[] arr = {"0","1","2","3","4","5"};
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i <arr.length ; i++) {
            if (i == arr.length-1){
                builder.append(arr[i]);
            }
            else {
                builder.append(arr[i]+",");
            }

        }

        System.out.println(builder.toString());

    }
    public boolean solution(String S, String T) {
        ArrayList<Character> listS = new ArrayList<Character>();
        ArrayList<Character> listT = new ArrayList<Character>();
        for(int i=0;i<S.length();i++){
            int number = 0;
            if(Character.isDigit(S.charAt(i))){
                number = S.charAt(i)-'0';
                while(S.length()>i+1 && Character.isDigit(S.charAt(i+1))){
                    number = number*10+ (S.charAt(i+1)-'0');
                    i++;
                }
                for(int j = 0 ;j<number ;j++){
                    listS.add('.');
                }
                i++;
            }
            if(S.length()>i)
            listS.add(S.charAt(i));
        }

        for(int i=0;i<T.length();i++){
            int number = 0;
            if(Character.isDigit(T.charAt(i))){
                number = T.charAt(i)-'0';
                while(T.length()>i+1 && Character.isDigit(T.charAt(i+1))){
                    number = number*10+ (T.charAt(i+1)-'0');
                    i++;
                }
                for(int j = 0 ;j<number ;j++){
                    listT.add('.');
                }
                i++;
            }
            if(T.length()>i)
            listT.add(T.charAt(i));
        }
        if(listS.size() != listT.size()) return false;
        for(int i =0;i<listS.size();i++){
            if(listS.get(i)!='.'){
                if(listT.get(i)!='.'){
                    if(listS.get(i)!=listT.get(i))
                        return false;
                }
            }
        }
        return true;
    }

}