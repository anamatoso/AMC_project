package projetoAMC;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class BN implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Grafo g;
	Amostrad a;
	double s;
	ArrayList<ArrayList<Double>> teta;

	public BN(Grafo g, Amostrad a, double s) throws Exception{
		super();
		this.g = g;
		this.a = a;
		this.s = s;

		int np=a.element(0).length-1;


		this.teta=new ArrayList<ArrayList<Double>>(np);
		for (int i = 0; i < np; i++)
			teta.add(new ArrayList<Double>());

		for (int i = 0; i < np; i++) { 				//para todas as variaveis exceto classe
			ArrayList<Integer> pais=g.parents(i); 		//posiçoes dos pais em arraylist

			//doms= vetor com dominios da variavel e dos pais
			int doms[]=new int[1+pais.size()];						//dominio da variaveis
			int vi[]= {i};
			doms[0]=a.domain(vi);
			for (int j = 0; j < pais.size(); j++) {  
				int p[]= {pais.get(j)};
				doms[j+1]=a.domain(p);
			}

			int dist[][]=distlist(doms); //possibilidades de variavel e respetivos pais

			for (int p = 0; p < dist.length; p++) {		//para todas as possibilidades


				int posp[]=pais.stream().mapToInt(Integer::intValue).toArray();		//posiçoes dos pais em vetor
				int posxp[]=new int[1+pais.size()];									//posicoes do x e pais
				posxp[0]=i;
				for (int j = 1; j < posxp.length; j++) {posxp[j]=posp[j-1];}	

				int xpval[]=new int[1+pais.size()];
				for (int j = 0; j < xpval.length; j++) {xpval[j]=dist[p][j];}

				int pval[]=new int[pais.size()];
				for (int j = 0; j < pval.length; j++) {pval[j]=dist[p][j+1];}

				double tetaip=(a.count(posxp, xpval)+s)/(a.count(posp, pval)+s*a.domain(vi));
				this.teta.get(i).add(p, tetaip); 
			}
		}	
	}

	private int posdistlist(int val [], int var) throws Exception {
		int vi[]= {var};
		int doms[]=new int[val.length];
		int []pospais=g.parents(var).stream().mapToInt(Integer::intValue).toArray();
		doms[0]=a.domain(vi);
		for (int j = 0; j < pospais.length; j++) {
			int p[]= {pospais[j]};
			doms[j+1]=a.domain(p);
		}

		int dist [][]=distlist(doms);
		boolean found=false;
		int pos=0;
		while (!found && pos<dist.length) {
			if (Arrays.equals(dist[pos], val)) {found=true;}
			else {pos++;}
		}
		return pos;
	}


	public double prob (int[] vet) throws Exception {
		int np=a.element(0).length-1;
		int npvet[]= {np};                    //posiçao da classe em vetor
		int cvet[]= {vet[np]};                    //com cancro em vetor
		double c=a.count(npvet, cvet);        //# com cancro
		double pc=(c+s)/(a.length()+s*a.domain(npvet));             // dá a probabilidade de a classe ser c cancro
		double sum=0;
		for (int i=0; i<vet.length-1; i++) {    //passa por todas  as pos do vetor {C}

			int[] pospais=g.parents(i).stream().mapToInt(Integer::intValue).toArray();
			int[]  val=new int[pospais.length+1];
			val[0]=vet[i];//primeira pos é o valor de Xi
			for (int k=0; k<pospais.length; k++) {//restantes posicçoes pais de Xi
				val[k+1]=vet[pospais[k]];
			}
			int pos=posdistlist(val,i);                //posiçao na distlist de cada val
			sum=sum+Math.log10(teta.get(i).get(pos));    //vai a linha em que di=Xi e coluna pos
		} 
		return pc*Math.pow(10, sum);
	}


	@Override
	public String toString() {
		return "BN [teta=" + teta + "]";
	}


	//copiada da classe Grafo
	private int[][] distlist(int dominios[]) { //retorna as varias combinaçoes possiveis de variaveis com os dominios no vetor "dominio"
		int d=1;
		for (int i : dominios) {d=d*i;}	//d vsi ser o numero de possiveis combinaçoes entre as variaveis
		int res[][]= new int [d][dominios.length];	//inicializa a matriz que em cada linha tem um possibilidade de combinaçao
		for (int i = 0; i < d; i++) {
			int a=i;
			for (int j = 0; j <dominios.length; j++) {
				int x=a;
				a=a%dominios[dominios.length-1-j];
				res[i][dominios.length-1-j]=a;
				a=(x-a)/dominios[dominios.length-1-j];
			}
		}
		return res;
	}

	public void save(String filename) throws IOException{
		FileOutputStream f= new FileOutputStream(new File(filename));
		ObjectOutputStream o= new ObjectOutputStream(f);
		o.writeObject(this);
		o.close();
		f.close();

	}

	public static void main(String[] args) {

	}
}
