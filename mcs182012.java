
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;


class State{
    
   int[][] al;
   int k,p,t;
    State(int k, int p, int t){
        this.k=k;
        this.p=p;
        this.t=t;
       
        al=new int[t*p][k];
    }
    
   
    State nstate(State s){
        State temp=new State(k,p,t);
        temp.al=new int[t*p][k];
        
        
        for (int i = 0; i < t*p; i++) {
            for (int j = 0; j < k; j++) {
                temp.al[i][j]=s.al[i][j];
            }
        }
        return temp;
    }
    
}



class NewClass{
    
    int gen_rand_int(int min, int max) {
    Random r = new Random();
    return r.nextInt((max - min) + 1) + min;
}
    
    State gen_rand_state(int k,int p,int t){
        State obj=new State(k,p,t);
        HashMap<Integer,Boolean> hm=new HashMap<>();
        int temp=0;
        for (int i = 0; i < t*p; i++) {
            
                for (int l = 0; l < k; l++) {
                    
                    do{
                    temp=gen_rand_int(0,p*k*t-1);
                    }while(hm.containsKey(temp));
                    hm.put(temp, Boolean.TRUE);
                    obj.al[i][l]=temp;
                }
            
        }
     return obj;   
    }
    
    
    State[] gen_frontier(int k, int p, int t, State curr){
        int temp= p*t;
        State[] st=new State[(int)Math.pow(((k*temp*(temp-1))>>1),k)];
        int x=0;
        for (int i = 0; i < temp-1; i++) {
            for (int j = 0; j < k; j++) {
                
                for (int l = i+1; l < temp; l++) {
                    for (int m = 0; m < k; m++) {
                        State op=new State(k,p,t);
                        op=op.nstate(curr);
                       //  System.out.println(op.al[i][j]+" "+op.al[l][m]);
                       //swap procedure 
                       int tt=op.al[i][j];
                        op.al[i][j]=op.al[l][m];
                        op.al[l][m]=tt;
                        
                //System.out.println(op.al[i][j]+" "+op.al[l][m]);
                        //st[x]=new State(k,p,t);
                        st[x++]=op.nstate(op);
                        
                        
                    }
                }
                
            }
        }
        return st;
        }
        
       double compute_h(State s, int C, double[][] ar,int k,int p,int t){
           double value=0.0,value2=0.0;
           
           for (int i = 0; i <p*t ; i++) {
               for (int j = 0; j < k-1; j++) {
                   for (int l = j+1; l < k; l++) {
                       value+=(1-ar[s.al[i][j]][s.al[i][l]]);
                   }
               }
           }
           
           for (int i = 0; i < p*t-1; i++) {
               for (int j = 0; j < k; j++) {
                   
                   for (int l = i+1; l < p*t; l++) {
                       for (int m = 0; m < k; m++) {
                           value2+=ar[s.al[i][j]][s.al[l][m]];
                       }
                   }
                   
               }
           }
        return value + C*value2;   
       } 
    
    double maxH=0.0;State ans=null;
       double solve(State s,int k,int p,int t,int C,double[][] ar){
          State[] st=gen_frontier(k,p,t,s);
          
           for (int i = 0; i < st.length; i++) {
               double ch=0.0;
               if(st[i]!=null){ ch=compute_h(st[i],C,ar,k,p,t);
               if( ch>maxH){
                   maxH=ch;
                   ans=st[i];
               }
               }
           }
           
           return maxH;
       }
       
       State solver(int k,int p,int t){
           
           return gen_rand_state(k, p, t);
       }
       
 public static void main(String... args) throws IOException{
     
    long startTime=System.currentTimeMillis();
    Scanner sc=new Scanner(new FileReader(args[0]));
    PrintWriter pw=new PrintWriter(new FileWriter(args[1]));
    
    double l = sc.nextDouble(); //time limit
    int k = sc.nextInt(); //no of papers per session
    int p = sc.nextInt(); //no of parallel sessions
    int t = sc.nextInt(); //no of time slots
    int C = sc.nextInt(); //trade-off parameter
    
    int pkt = k*p*t; // total no of papers
    
    double[][] ar=new double[pkt][pkt];
    
     for (int i = 0; i < pkt; i++) {
          for (int j = 0; j < pkt; j++) {
             ar[i][j]=sc.nextDouble();
         }        
     }
    
     State ans=null;;
     
     long endTime=startTime + (long)(l*1000 - 10);
     NewClass obj=new NewClass();
     double maxPrev=0.0,maxNew=0.0;

     while(System.currentTimeMillis()<endTime){
     State start=obj.solver(k,p,t); int maxLoop=5;
     obj.ans=start;
     do{
     maxPrev=maxNew;    
     maxNew=obj.solve(obj.ans, k, p, t, C, ar);
     if(maxPrev==maxNew) maxLoop--;
         //System.out.println(maxPrev+" "+maxNew);
     }while(System.currentTimeMillis()<endTime && maxLoop>0 && maxNew>=maxPrev);
     }
     
     for (int i = 1; i <= t*p; i++) {
         for (int j = 0; j < k; j++) {
             pw.print(obj.ans.al[i-1][j]+" ");
         }if(i!=t*p && i%(p)!=0)pw.print("| ");
         if(i%p==0 && i!=t*p) pw.println();
         pw.flush();
     }
     pw.println("");
       pw.flush();pw.close();
             
             /*
             for (int i = 0; i < st.length; i++) {
                 for (int j = 0; j < t*p; j++) {
                     for (int m = 0; m < k; m++) {
                         System.out.print(st[i].al[j][m]+",");
                     }
                     System.out.print("|");
                 }System.out.println("");
     }*/
             
             
             
     
 }

    
}

