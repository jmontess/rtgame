package com.jms.trgame;

import com.badlogic.gdx.math.MathUtils;

/**
 * Created by jmontes on 8/12/16.
 */
public class MazeGenerator {

    private int n;                 // dimension of maze
    private boolean[][] north;     // is there a wall to north of cell i, j
    private boolean[][] east;
    private boolean[][] south;
    private boolean[][] west;
    private boolean[][] visited;

    public MazeGenerator(int n) {
        this.n = n;

        // initialize border cells as already visited
        visited = new boolean[n+2][n+2];
        for (int x = 0; x < n+2; x++) {
            visited[x][0] = true;
            visited[x][n+1] = true;
        }
        for (int y = 0; y < n+2; y++) {
            visited[0][y] = true;
            visited[n+1][y] = true;
        }


        // initialze all walls as present
        north = new boolean[n+2][n+2];
        east  = new boolean[n+2][n+2];
        south = new boolean[n+2][n+2];
        west  = new boolean[n+2][n+2];
        for (int x = 0; x < n+2; x++) {
            for (int y = 0; y < n+2; y++) {
                north[x][y] = true;
                east[x][y]  = true;
                south[x][y] = true;
                west[x][y]  = true;
            }
        }

        generate();
    }

    // generate the maze starting from lower left
    private void generate() {
        generate(1, 1);
    }

    private void generate(int x, int y) {

        visited[x][y] = true;

        // while there is an unvisited neighbor
        while (!visited[x][y+1] || !visited[x+1][y] || !visited[x][y-1] || !visited[x-1][y]) {

            // pick random neighbor (could use Knuth's trick instead)
            while (true) {
                double r = MathUtils.random(0,3);
                if (r == 0 && !visited[x][y+1]) {
                    north[x][y] = false;
                    south[x][y+1] = false;
                    generate(x, y + 1);
                    break;
                }
                else if (r == 1 && !visited[x+1][y]) {
                    east[x][y] = false;
                    west[x+1][y] = false;
                    generate(x+1, y);
                    break;
                }
                else if (r == 2 && !visited[x][y-1]) {
                    south[x][y] = false;
                    north[x][y-1] = false;
                    generate(x, y-1);
                    break;
                }
                else if (r == 3 && !visited[x-1][y]) {
                    west[x][y] = false;
                    east[x-1][y] = false;
                    generate(x-1, y);
                    break;
                }
            }
        }
    }

    public void print() {

        boolean[][] m = new boolean[n*2][n*2];
        for (int i = 0; i < n*2; i++) {
            for (int j = 0; j< n*2; j++) {
                m[i][j] = false;
            }
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j< n; j++) {
                int pi = i*2, pj = j*2;
                if (north[i][j]) m[pi][pj]   = true;
                if (south[i][j]) m[pi+1][pj] = true;
                if (east[i][j])  m[pi][pj]   = true;
                if (west[i][j])  m[pi][pj+1] = true;
            }
        }

        System.out.println("*************************************");
        for (int i = n*2-1; i >= 0; i--) {
            System.out.print("*");
            for (int j = 0; j< n*2; j++) {
                char c = ' ';
                if (m[i][j]) c = '*';
                System.out.print(c);
            }
            System.out.print("*\n");
        }
        System.out.println("*************************************");
    }

}
