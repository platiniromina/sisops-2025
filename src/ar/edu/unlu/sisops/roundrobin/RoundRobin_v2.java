package ar.edu.unlu.sisops.roundrobin;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

public class RoundRobin_v2 {

    private static final float Q = 4;
    private static final ArrayList<Proceso> procesos = new ArrayList<>();

    static class Proceso {
        String id;
        double tiempoDeServicio;
        double tiempoRestante;

        public Proceso(String id, int tiempoDeServicio) {
            this.id = id;
            this.tiempoDeServicio = tiempoDeServicio;
            this.tiempoRestante = tiempoDeServicio;
        }
    }

    public static void agregarProceso(String id, int tiempoDeServicio) {
        Proceso p = new Proceso(id, tiempoDeServicio);
        procesos.add(p);
    }

    static class Registro {
        Proceso proceso;
        double tiempoDeRetorno;
        double tiempoDeEspera;

        public Registro(Proceso proceso, double tiempoDeEspera, double tiempoDeRetorno) {
            this.proceso = proceso;
            this.tiempoDeEspera = tiempoDeEspera;
            this.tiempoDeRetorno = tiempoDeRetorno;
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        boolean exit = false;


        while ( !exit ) {
            System.out.println("\tMenú Round Robin");
            System.out.println("1. Comenzar round robin (con los procesos cargados)");
            System.out.println("2. Cargar más procesos");
            System.out.println("3. Salir");
            System.out.printf("Ingrese una opción: ");
            int op = sc.nextInt();
            sc.nextLine();
            switch (op) {
                case 1 -> exit = roundRobin();
                case 2 -> {
                    System.out.print("Ingrese id del proceso: ");
                    String id = sc.nextLine();
                    System.out.print("Ingrese tiempo de servicio del proceso: ");
                    int tiempoDeServicio = sc.nextInt();
                    agregarProceso(id, tiempoDeServicio);
                }
                case 3 -> exit = true;
                default -> System.out.println("Ingrese un valor válido.");
            }

        }
        sc.close();
    }

    public static boolean roundRobin() {

        double tiempoDeIntercambio = Q / 4;
        double reloj = 0;

        ArrayList<Registro> registros = new ArrayList<>();

        Proceso procesoAnt = null;

        imprimirInicioRoundRobin();

        // Mientras haya procesos en la cola
        while ( !procesos.isEmpty() ) {
            // Usamos un iterator para poder eliminar procesos terminados mientras iteramos
            Iterator<Proceso> iterator = procesos.iterator();

            while ( iterator.hasNext() ) {
                Proceso proceso = iterator.next();

                // Si el proceso es distinto del anterior y si el anterior no es nulo (es decir el primero), se desaloja
                if ( procesoAnt != proceso && procesoAnt != null ) {
                    reloj += tiempoDeIntercambio / 2;
                }

                // Si el tiempo restante del proceso es mayor a Q, se asigna al procesador
                if ( proceso.tiempoRestante > Q ) {
                    reloj += tiempoDeIntercambio / 2;

                    // Se atiende el proceso, sumo Q al reloj y resto Q al tiempo restante
                    proceso.tiempoRestante -= Q;
                    reloj += Q;
                }
                // El tiempo restante del proceso actual es menor que Q, se pone en 0 el tiempo restante
                // No se hace el intercambio
                else {
                    reloj += proceso.tiempoRestante;
                    proceso.tiempoRestante = 0;

                    System.out.printf("Proceso %s terminado %n", proceso.id);
                    System.out.printf("Reloj: %.2f %n", reloj);

                    // Almaceno en registros el proceso junto con su tiempo de espera y su tiempo de retorno
                    Registro registro = new Registro(proceso, reloj - proceso.tiempoDeServicio, reloj);
                    registros.add(registro);

                    // Quito el proceso de la cola, porque ya se terminó de atender
                    iterator.remove();

                }
                procesoAnt = proceso;

            }
        }
        imprimirFinRoundRobin();
        // Calcular tiempo de espera promedio
        mostrarResultados(registros);

        return true;
    }

    private static void mostrarResultados(ArrayList<Registro> registros) {
        double tiempoDeEsperaPromedio = registros.stream()
                .mapToDouble(r -> r.tiempoDeEspera).average().orElse(0);
        double tiempoDeRetornoPromedio = registros.stream()
                .mapToDouble(r -> r.tiempoDeRetorno).average().orElse(0);

        System.out.println("---------------------------------");
        System.out.println("           RESULTADOS");
        System.out.println("---------------------------------");
        System.out.printf("Tiempo de espera promedio: %.2f%n", tiempoDeEsperaPromedio);
        System.out.printf("Tiempo de retorno promedio: %.2f%n", tiempoDeRetornoPromedio);
    }

    public static void imprimirInicioRoundRobin() {
        System.out.println();
        System.out.println("---------------------------------");
        System.out.println("       INICIO ROUND ROBIN");
        System.out.println("---------------------------------");
        System.out.println();
    }

    public static void imprimirFinRoundRobin() {
        System.out.println();
        System.out.println("---------------------------------");
        System.out.println("        FIN ROUND ROBIN");
        System.out.println("---------------------------------");
        System.out.println();
    }
}

