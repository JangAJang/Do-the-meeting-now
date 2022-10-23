package com.swprogramming.dothemeetingnow;

public class Graph{
    private int n;
    private Long maps[][];

    public Graph(int n){
        this.n = n;
        maps = new Long[n][n];
        for(int i=0; i<n; ++i){
            for(int j=0; j<n; ++j){
                maps[i][j] = Long.MAX_VALUE;
            }
        }
    }

    public void input(int i,int j,Long w){
        maps[i][j] = w;
        maps[j][i] = w;
    }

    public Long[] dijkstra(int v){
        Long distance[] = new Long[n];
        boolean[] check = new boolean[n];
        for(int i=0; i<n; ++i){
            distance[i] = Long.MAX_VALUE;
        }
        distance[v] = 0L;
        check[v] = true;
        for(int i=0; i<n; ++i){
            if(!check[i] && maps[v][i] != Integer.MAX_VALUE){
                distance[i] = maps[v][i];
            }
        }
        for(int a=0; a<n-1; ++a){
            Long min = Long.MAX_VALUE;
            int min_index = -1;
            for(int i=0; i<n; ++i){
                if(!check[i]){
                    if(distance[i] < min){
                        min = distance[i];
                        min_index = i;
                    }
                }
            }
            check[min_index] = true;
            for(int i=0; i<n; ++i){
                if(!check[i] && maps[min_index][i] != Integer.MAX_VALUE){
                    if(distance[min_index] + maps[min_index][i] < distance[i]){
                        distance[i] = distance[min_index] + maps[min_index][i];
                    }
                }
            }
        }
        return distance;
    }
}
