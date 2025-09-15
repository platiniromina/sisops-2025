#include <stdio.h>
#include <stdlib.h>
#include <string.h>

typedef struct Proceso {
    int PID;
    double tiempoDeServicio;
    double tiempoRestante;
} proceso;

typedef struct Registro {
    int PID;
    double tiempoDeEspera;
    double tiempoDeRetorno;
} registro;

int sumaTiempoRestante(proceso procesos[], int cantProcesos) {
    int suma = 0;
    for (int i = 0; i < cantProcesos; i++) {
        suma += procesos[i].tiempoRestante;
    }
    return suma;
}

void roundRobin(proceso procesos[], registro registros[], int cantProcesos, int quantum) {
    double reloj = 0;
    double tiempoDeIntercambio = quantum / 4;
    int cantRegistros = 0;

    proceso *procesoAnt = NULL;

    do {

        for (int i = 0; i < cantProcesos; i++) {
        
            // si ya se terminó de atender, se sigue con el próximo proceso
            if (procesos[i].tiempoRestante == 0) {
                continue;
            }

            // si no es el primer proceso, o es diferente al anterior, se desaloja
            if (procesoAnt != NULL && &procesos[i] != procesoAnt) {
                reloj += tiempoDeIntercambio / 2;
            }

            // si el tiempo restante es mayor al quantum, se asigna al procesador
            if (procesos[i].tiempoRestante > quantum) {
                reloj += tiempoDeIntercambio / 2;

                // Se atiende el proceso, sumo Q al reloj y resto Q al tiempo restante
                reloj += quantum;
                procesos[i].tiempoRestante -= quantum;
            } 
            // El tiempo restante del proceso actual es menor que Q, se pone en 0 el tiempo restante
            // No se hace el intercambio
            else {
                reloj += procesos[i].tiempoRestante;
                procesos[i].tiempoRestante = 0;

                printf("\nProceso %d terminado", procesos[i].PID);
                printf("\nReloj: %.2f \n", reloj);

                // Almaceno en registros el proceso junto con su tiempo de espera y su tiempo de retorno
                registros[cantRegistros].PID = procesos[i].PID;
                registros[cantRegistros].tiempoDeEspera = reloj - procesos[i].tiempoDeServicio;
                registros[cantRegistros].tiempoDeRetorno = reloj;
                cantRegistros++;
            }
            procesoAnt = &procesos[i];
        }

    // mientras haya procesos por atender
    } while (sumaTiempoRestante(procesos, cantProcesos) > 0);
}

void mostrarResultados(registro registros[], int cantRegistros) {
    double tiempoDeEsperaPromedio = 0, tiempoDeRetornoPromedio = 0;

    for (int i = 0; i < cantRegistros; i++) {
        tiempoDeEsperaPromedio += registros[i].tiempoDeEspera;
        tiempoDeRetornoPromedio += registros[i].tiempoDeRetorno;
    }

    tiempoDeEsperaPromedio /= cantRegistros;
    tiempoDeRetornoPromedio /= cantRegistros;

    printf("--------------------------------- \n");
    printf("           RESULTADOS \n");
    printf("--------------------------------- \n");
    printf("Tiempo de espera promedio: %.2f \n", tiempoDeEsperaPromedio);
    printf("Tiempo de retorno promedio: %.2f \n", tiempoDeRetornoPromedio);
}

void imprimirInicioRoundRobin() {
        printf("\n---------------------------------\n");
        printf("       INICIO ROUND ROBIN \n");
        printf("---------------------------------\n");
    }

void imprimirFinRoundRobin() {
    printf("\n---------------------------------\n");
    printf("        FIN ROUND ROBIN \n");
    printf("---------------------------------\n\n");
}

int main(int argc, char **argv) {

    // argv[0] es el nombre del programa, arg[1] es el quantum
    int quantum = atoi(argv[1]);
    int cantProcesos = argc - 2;

    registro *registros = malloc(sizeof(registro) * cantProcesos);

    proceso *procesos = malloc(sizeof(proceso) * cantProcesos);
    for (int i = 0; i < cantProcesos; i++) {
        procesos[i].PID = i + 1;
        procesos[i].tiempoDeServicio = atoi(argv[i + 2]);
        procesos[i].tiempoRestante = procesos[i].tiempoDeServicio;
    }

    imprimirInicioRoundRobin();

    roundRobin(procesos, registros, cantProcesos, quantum);

    imprimirFinRoundRobin();

    mostrarResultados(registros, cantProcesos);

    return 0;
}

