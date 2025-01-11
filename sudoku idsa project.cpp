#include <iostream>


using namespace std;

void output(int grid[9][9])
{
	cout << endl;
    for (int i = 0; i < 9; ++i)
    {
        for (int j = 0; j < 9; ++j)
        {
    		cout << grid[i][j];
    		if(j <8)
    		{
    			cout << " ";
			}
    	}
    	 cout << "\n";
    }
   
}

bool canput(int grid[9][9], int r, int c, int zen)
{
	 for(int i = 0; i <= 8; ++i)
    {
        if(grid[r][i] == zen)
        {
            return false;
        }
    }
	
	for(int k =0; k <=8; ++k)
    {
    	if(grid[k][c] == zen)
    	{
    		return false;
		}
	}

    	int begr = r-r % 3; //ensures we work in a 3*3 grid basis.
    	int begc = c-c % 3;

    for(int n = 0; n < 3; ++n)
    {
        for(int k = 0; k < 3; ++k)
        {
            if (grid[n+begr][k+begc] == zen)
            {
                    return false;
            }
        }
     }
    return true;
}


bool solve(int grid[9][9], int r, int c)
{
    if(r == 8 && c ==9)
    {
        return true;
    }

    if(c == 9)
    {
    	++r;
    	c = 0;
	}

   if(grid[r][c] >0)
   {
       return solve(grid, r, c+1);
   }

   for(int j=1; j<=9; ++j)
   {
       if(canput(grid, r, c, j))
       {
           grid[r][c] = j;
           if(solve(grid, r, c+1))
           {
               return true;
           }
       }
       grid[r][c]=0;

   }
   return false;

}


int main(){

	int grid[9][9] = {};

	for(int i =0; i <9; ++i)
	{
		for(int j =0; j < 9; ++j)
		{
			cin >> grid[i][j];
		}	
	}

    if(solve(grid, 0, 0))
    {
        output(grid);
    }
    else
    {
       cout << "No Solution" << endl;
    }
}


