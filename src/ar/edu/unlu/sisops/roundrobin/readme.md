## Compilar y ejecutar roundrobin.c

Para compilar, crear una carpeta llamada `build` y ejecutar: 
```
clang -g src/ar/edu/unlu/sisops/roundrobin/roundrobin.c -o build/roundrobin
```

Luego para correr el programa:
```
./build/roundrobin [quantum] [tiempo_proceso_1] ... [tiempo_proceso_N]
```