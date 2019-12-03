package calculadora;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;

public class calculadora
{

    static final HashMap<String, Integer> tipo;
	private static Scanner scal;
	static
    {
        tipo = new HashMap<>();
        tipo.put("^", 3);
        tipo.put("%", 2);
        tipo.put("*", 2);
        tipo.put("/", 2);
        tipo.put("+", 1);
        tipo.put("-", 1);
    }

    public static void main(String[] args)
    {

        Queue<String> calc = new LinkedList<>(); 
        scal = new Scanner(System.in);
        Character c, c2 = ' ';
        String det;
        String digito = "";
        do
        {
            System.out.println("Insira a expressão: ");
            det = scal.nextLine();
            det = det.replaceAll(" ", "");
            if (det.equals("sair"))
            {
                System.exit(0);
            }

            for (int i = 0; i < det.length(); i++)
            {
                c = det.charAt(i);
                if (i + 1 < det.length())
                {
                    c2 = det.charAt(i + 1);
                }

                if (c.equals('(') || c.equals(')'))
                {
                    if (c.equals('(') && c2.equals('-'))
                    {
                        System.out.println("negativo :)");
                        main(args);
                    } else
                    {
                        calc.add(c.toString());
                    }
                } else if (!Character.isDigit(c))
                {
                    if (calc.isEmpty() && c.equals('-'))
                    {
                        System.out.println("negativo :)");
                        main(args);
                    } else if (c2.equals('-'))
                    {
                        System.out.println("negativo :)");
                        main(args);
                    } else
                    {
                        calc.add(c.toString());
                    }
                } else if (Character.isDigit(c))
                {
                    if (i + 1 < det.length() && det.charAt(i + 1) == '.') 
                    {
                        int j = i + 1;
                        digito = c.toString() + det.charAt(j); 
                        while (j + 1 <= det.length() - 1 && Character.isDigit(det.charAt(j + 1)))
                        {
                            digito = digito + det.charAt(j + 1);
                            j++;
                        }
                        i = j;
                        calc.add(digito);
                        digito = "";
                    } else if (i + 1 <= det.length() - 1 && Character.isDigit(det.charAt(i + 1)))
                    {
                        int j = i;
            
                        while (j <= det.length() - 1 && Character.isDigit(det.charAt(j)))
                        {
                        	digito = digito + det.charAt(j);
                            j++;
                        }
                        i = j - 1;
                        calc.add(digito);
                        digito = "";
                    } else
                    {
                        calc.add(c.toString());
                    }

                }
            }

            troca(calc);
        } while (!det.equals("sair"));
    }

    
    public static void troca(Queue<String> calc)
    {
        Stack<String> operador = new Stack<String>();
        Queue<String> posfixa = new LinkedList<>();
        String t;
        while (!calc.isEmpty())
        {
            t = calc.poll();
            try
            {
                Double.parseDouble(t);
                posfixa.add(t);
            } catch (NumberFormatException nfe)
            {
                if (operador.isEmpty())
                {
                    operador.add(t);
                } else if (t.equals("("))
                {
                    operador.add(t);
                } else if (t.equals(")"))
                {
                    while (!operador.peek().toString().equals("("))
                    {
                        posfixa.add(operador.peek().toString());
                        operador.pop();
                    }
                    operador.pop();
                } else
                {
                    while (!operador.empty() && !operador.peek().toString().equals("(") && tipo.get(t) <= tipo.get(operador.peek().toString()))
                    {
                        posfixa.add(operador.peek().toString());
                        operador.pop();
                    }
                    operador.push(t);
                }
            }
        }
        while (!operador.empty())
        {
            posfixa.add(operador.peek().toString());
            operador.pop();
        }
        System.out.println();
        System.out.println("Em posfixa a expressão é: ");
        
        for (String v : posfixa)
        {
            System.out.print(v + " ");
        }
        evoluposfixa(posfixa);
    }

    
    public static void evoluposfixa(Queue<String> posfixa)
    {
        Stack<String> ev = new Stack<>(); 
        String t;
        Double nume1, nume2, resultado = 0.0;
        while (!posfixa.isEmpty())
        {
            t = posfixa.poll();
            try
            {
                Double.parseDouble(t);
                ev.add(t);
            } catch (NumberFormatException nfe)
            {
                nume1 = Double.parseDouble(ev.peek());
                ev.pop();
                nume2 = Double.parseDouble(ev.peek());
                ev.pop();

                switch (t)
                {
                    case "+":
                        resultado = nume2 + nume1;
                        break;
                    case "-":
                        resultado = nume2 - nume1;
                        break;
                    case "*":
                        resultado = nume2 * nume1;
                        break;
                    case "/":
                       
                        if (nume1 == 0)
                        {
                            System.out.println("impossivel dividir por zero");
                            return;
                        } else
                        {
                            resultado = nume2 / nume1;
                            break;
                        }
                    case "%":
                        resultado = nume2 % nume1;
                        break;
                    case "^":
                        resultado = Math.pow(nume2, nume1);
                        break;

                }

                ev.push(resultado.toString());

            }

        }
       
        System.out.println("O Resultado é: ");
        DecimalFormat fd = new DecimalFormat("0.000");
        for (String v : ev)
        {
            System.out.println(fd.format(Double.parseDouble(v)) + "\n");
        }
    }

}

