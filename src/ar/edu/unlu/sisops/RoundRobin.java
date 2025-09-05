import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Scanner;

public class RoundRobin {

    static class Proceso{
        int id;
        int tiempoDeServicio;
        int tiempoRestante;
        int tiempoDeRetorno; //Tiempo total. desde que entró hasta que salió
        int tiempoDeEspera; //Para calcular el tiempo de espera. Tiempo en que no fue atendido

        public Proceso(int id, int tiempoDeServicio){
            this.id = id;
            this.tiempoDeServicio= tiempoDeServicio;
            this.tiempoRestante = tiempoDeServicio;
        }
    }

    static class Registro{
        Proceso proceso;
        float tiempoDeRetorno;
        float tiempoDeEspera;
    }



    private static final float q = 4;
    private static ArrayList<Proceso> procesos = new ArrayList<>();

    public static void agregarProceso(int id, int tiempoDeServicio){
        Proceso p = new Proceso(id, tiempoDeServicio);
        procesos.add(p);
    }
    public static void roundRobin(){
        float tiempoDeIntercambio = q/4;
        float reloj=0;
        ArrayList<Registro> registros = new ArrayList<>();
        Registro registro = new Registro();
        int sumaTiempoRestante = procesos.stream().mapToInt(p ->p.tiempoRestante).sum();
        Proceso procesoAnt = null;
        while(sumaTiempoRestante > 0){
            for (Proceso proceso: procesos) {
                //Si al proceso actual tiene más que Q
                if(proceso.tiempoRestante>q){
                    //Si el proceso es distinto del anterior y si el anterior no es nulo
                    if(procesoAnt != proceso && procesoAnt != null){

                        reloj += tiempoDeIntercambio/2;
                    }
                    //Si el proceso.tiempoRestante es mayor a 4
                    if(proceso.tiempoRestante>q){

                        reloj += tiempoDeIntercambio/2;
                    }

                    //proceso el proceso, sumo al reloj y al tiempo restante
                    proceso.tiempoRestante -= q;
                    reloj += q;

                //else el proceso actual tiene menos que Q
                }else {
                    //Si el proceso es distinto del proceso anterior
                    if(proceso != procesoAnt){
                        reloj += tiempoDeIntercambio/2;
                    }

                    reloj += proceso.tiempoRestante;
                    proceso.tiempoRestante = 0;

                    System.out.println("proceso:"+proceso.id+"ha sido terminado"+"\nReloj:"+reloj);
                    //almaceno en registro el proceso junto con su tiempo de espera y su tiempo de retorno
                    registro.proceso = proceso;
                    registro.tiempoDeEspera = reloj - proceso.tiempoDeServicio;
                    registro.tiempoDeRetorno = reloj;
                    registros.add(registro);
                }
                procesoAnt = proceso;
            }
            sumaTiempoRestante = procesos.stream().mapToInt(p ->p.tiempoRestante).sum();
        }

        //Calcular tiempo de espera promedio
        double tiempoDeEsperaPromedio = registros.stream().mapToDouble(r -> r.tiempoDeEspera).average().getAsDouble();
        double tiempoDeRetornoPromedio = registros.stream().mapToDouble(r -> r.tiempoDeRetorno).average().getAsDouble();

        //Mostrar los tiempos promedios
        System.out.println("Tiempo de espera promedio:"+tiempoDeEsperaPromedio+"\nTiempo de retorno promedio:"+tiempoDeRetornoPromedio);

    }

    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        boolean exit = false;


        while(!exit){
            System.out.println("Ingrese una opción:");
            System.out.println("1.Comenzar RoundRobin (con los procesos cargados)");
            System.out.println("2.Cargar más procesos");
            System.out.println("3.Salir");
            int op = sc.nextInt();
            switch (op) {
                case 1 -> {
                    roundRobin();
                    //borrar procesos
                }
                case 2 -> {
                    System.out.println("Ingrese id del proceso:");
                    int id = sc.nextInt();
                    System.out.println("Ingrese tiempo de servicio del proceso:");
                    int tiempoDeServicio = sc.nextInt();
                    agregarProceso(id, tiempoDeServicio);
                }
                case 3 -> {
                    exit = true;
                }
                default -> System.out.println("Ingrese un valor válido.");
            }

        }
    }
}