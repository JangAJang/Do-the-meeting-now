package com.swprogramming.dothemeetingnow;

public class Graph{
    private int n;           // 노드들의 수
    private Long maps[][];    // 노드들간의 가중치 저장할 변수

    public Graph(int n){
        this.n = n;
        maps = new Long[n][n];

        // 인접행렬 모든 값 무한대로 초기화
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
        Long distance[] = new Long[n];          // 최단 거리를 저장할 변수
        boolean[] check = new boolean[n];     // 해당 노드를 방문했는지 체크할 변수

        // distance값 초기화. 무한대를 int 자료형의 최대값으로 표현했다.
        for(int i=0; i<n; ++i){
            distance[i] = Long.MAX_VALUE;
        }

        // 시작노드값 초기화.
        distance[v] = 0L;
        check[v] = true;

        // 연결노드 distance갱신
        for(int i=0; i<n; ++i){
            if(!check[i] && maps[v][i] != Integer.MAX_VALUE){
                distance[i] = maps[v][i];
            }
        }

        for(int a=0; a<n-1; ++a){
            Long min = Long.MAX_VALUE;
            int min_index = -1;

            // 노드 최소값 찾기
            for(int i=0; i<n; ++i){
                if(!check[i]){
                    if(distance[i] < min){
                        min = distance[i];
                        min_index = i;
                    }
                }
            }

            // 다른 노드를 거쳐서 가는 것이 더 비용이 적은지 확인한다.
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
