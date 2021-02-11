package projetoAMC;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class GrafoOti extends Grafo implements Serializable {

	private static final long serialVersionUID = 1L;

	public GrafoOti(int dim) {
		super(dim);
		// TODO Auto-generated constructor stub
	}

	@SuppressWarnings("unchecked")
	public double MDLdelta1 (Amostrad a, int o, int d, int op, ArrayList<Double> It ) throws Exception {//para so calcular
		GrafoOti galt= new GrafoOti (this.dim);                                                //os It que serão alterados
		ArrayList<Double> Italt= (ArrayList<Double>) It.clone();
		if (op==0 || op==1 || op==2) {
			ArrayList<Integer[]> el=this.EdgeList(); //cria uma cópia do grafo inicial e altera-se a cópia
			for (int i=0; i<el.size(); i++) {
				Integer[] edge=el.get(i);  		
				galt.addEdge(edge[0],edge[1]); 
			}			
			if (op==0) {
				galt.removeEdge(o, d);
				Italt.set(d, galt.infomut(d,a));
			}
			if (op==1) {
				galt.invertedge(o,d);
				Italt.set(d, galt.infomut(d,a));
				Italt.set(o, galt.infomut(o,a));
			}
			if (op==2) {
				galt.addEdge(o, d);
				Italt.set(d, galt.infomut(d,a));
			}
		}
		else 
			throw new Exception("Error in MDLdelta: int nao corresponde operaçao");

		double mdl = this.MDL1(a, It);
		double mdlalt = galt.MDL1(a, Italt);
		double delta= mdlalt-mdl; //isto tem de ser <0 para o grafo ser mlr
		return delta;
	}


	public double MDL1 (Amostrad a, ArrayList<Double> It) throws Exception {
		int np=a.element(0).length;
		double sum=0;
		for (int i=0; i<np-1; i++) {
			sum= sum+It.get(i);
		}
		return (Math.log10(a.length())/Math.log10(2)*theta(a)/2)-(a.length())*sum;
	}

	public ArrayList<Double> infomutlist(Amostrad a) throws Exception{ //cria uma lista com os valores de It
		int np=a.element(0).length-1;
		ArrayList<Double> It = new ArrayList<Double>();
		for (int i=0; i<np; i++) {
			It.add(this.infomut(i,a));
		}
		return It;
	}

	public static GrafoOti randomDAG(int k, int n) throws Exception { //cria um grafo aleatório
		GrafoOti  g = new GrafoOti(n);
		Random r = new Random();

		List<Integer> perm;
		perm = IntStream.range(0, g.adj.size() - 1).boxed().collect(Collectors.toList());
		java.util.Collections.shuffle(perm);


		for (int i = 0; i < g.adj.size() - 2; i++) {

			List<Integer> nodes;
			nodes = IntStream.range(i+1, g.adj.size() - 1).boxed().collect(Collectors.toList());
			int rand = r.nextInt(nodes.size());
			java.util.Collections.shuffle(nodes);

			for (int j = 0; j < rand; j++)
				if (g.parents(perm.get(nodes.get(j))).size() < k - 1)
					g.addEdge(perm.get(i), perm.get(nodes.get(j)));
		}
		for (int i = 0; i < g.adj.size() - 1; i++)
			g.addEdge(g.adj.size() - 1, i);

		return g;
	}

	protected GrafoOti copygraph() throws Exception{ //copia um grafo
		GrafoOti gfinal= new GrafoOti (this.dim);
		ArrayList<Integer[]> el=this.EdgeList();
		for (int i=0; i<el.size(); i++) {
			Integer[] edge=el.get(i);  		
			gfinal.addEdge(edge[0],edge[1]);
		}
		return gfinal;
	}

	@SuppressWarnings("unchecked")
	public static BN aprende(Amostrad a, int maxpais, int ngrafini) throws Exception{ //funçao responsavel pela aprendizagem
		int np=a.element(0).length-1;
		GrafoOti g1=new GrafoOti(np+1);
		for (int i = 0; i < np; i++) {g1.addEdge(np, i);} //adicionar as arestas para a classe ao grafo vazio
		ArrayList<Double> it=g1.infomutlist(a);
		//Melhorar grafo g1
		ArrayList<Integer> op= new ArrayList<Integer>(3); //{mdldelta,origem, destino, operaçao}
		double mdldelta1 = -1;
		op.add(0, 0);op.add(1, 0);op.add(2,0);
		System.out.println(g1);
		while (mdldelta1<-Math.pow(10, -13)) {
			mdldelta1=0;
			for (int i = 0; i < np; i++) {
				for (int j = 0; j < np; j++) {
					//nao ha aresta
					if (!g1.edgeQ(i,j) && !g1.connected(j,i) && g1.parents(j).size()<maxpais) {
						if (g1.MDLdelta1(a, i, j, 2,it)<mdldelta1) {
							mdldelta1=g1.MDLdelta1(a, i, j, 2,it);
							op.set(0,i);
							op.set(1,j);
							op.set(2,2);
						}
					}
					//ha aresta entre o1 e d1
					else {
						if (g1.MDLdelta1(a, i, j, 0,it)<mdldelta1) {
							mdldelta1=g1.MDLdelta1(a, i, j, 0,it);
							op.set(0,i);
							op.set(1,j);
							op.set(2,0);
						}
						else if (g1.parents(i).size()<maxpais && !g1.connected(i, j) && g1.MDLdelta1(a, i, j, 1,it)<mdldelta1) {
							mdldelta1=g1.MDLdelta1(a, i, j, 1,it);
							op.set(0,i);
							op.set(1,j);
							op.set(2,1);
						}
					}
				}
			}
			if (op.get(2)==0) {
				g1.removeEdge(op.get(0), op.get(1));
				it.set(op.get(1), g1.infomut(op.get(1),a));
			}
			else if (op.get(2)==1) { 
				g1.invertedge(op.get(0), op.get(1));
				it.set(op.get(1), g1.infomut(op.get(1),a));
				it.set(op.get(0), g1.infomut(op.get(0),a));

			}
			else if (op.get(2)==2) { 
				g1.addEdge(op.get(0), op.get(1));	
				it.set(op.get(1), g1.infomut(op.get(1),a));
			}
		}
		//ciclo para correr o numero de grafos exceto o vazio
		for (int i = 1; i < ngrafini; i++) {	
			//criar grafo aleatoriamente
			GrafoOti g2=randomDAG(maxpais,g1.dim);
			System.out.println("");
			ArrayList<Double> it2=g2.infomutlist(a);
			//Melhorar grafo g2
			ArrayList<Integer> op2= new ArrayList<Integer>(3); //{mdldelta,origem, destino, operaçao}
			op2.add(0, 0);op2.add(1, 0);op2.add(2,0);
			double mdldelta=-1;
			while (mdldelta<-Math.pow(10, -13)) {
				mdldelta=0;
				for (int i1 = 0; i1 < np; i1++) {
					for (int j = 0; j < np; j++) {
						//nao ha aresta
						if (!g2.edgeQ(i1,j) && !g2.connected(j, i1)&& g2.parents(j).size()<maxpais) {
							if (g2.MDLdelta1(a, i1, j, 2,it2)<mdldelta) {
								mdldelta=g2.MDLdelta1(a, i1, j, 2,it2);
								op2.set(0,i1);
								op2.set(1,j);
								op2.set(2,2);
							}
						}
						//ha aresta entre o1 e d1
						else {
							if (g2.MDLdelta1(a, i1, j, 0,it2)<mdldelta) {
								mdldelta=g2.MDLdelta1(a, i1, j, 0,it2);
								op2.set(0,i1);
								op2.set(1,j);
								op2.set(2,0);
							}
							else if (g2.parents(i1).size()<maxpais && !g2.connected(i1, j) && g2.MDLdelta1(a, i1, j, 1,it2)<mdldelta) {
								mdldelta=g2.MDLdelta1(a, i1, j, 1,it2);
								op2.set(0,i1);
								op2.set(1,j);
								op2.set(2,1);
							}
						}
					}
				}
				if (op2.get(2)==0) {
					g2.removeEdge(op2.get(0), op2.get(1));
					it2.set(op2.get(1), g2.infomut(op2.get(1),a));
				}
				else if (op2.get(2)==1) { 
					g2.invertedge(op2.get(0), op2.get(1));
					it2.set(op2.get(1), g2.infomut(op2.get(1),a));
					it2.set(op2.get(0), g2.infomut(op2.get(0),a));

				}
				else if (op2.get(2)==2) { 
					g2.addEdge(op2.get(0), op2.get(1));	
					it2.set(op2.get(1), g2.infomut(op2.get(1),a));
				}
			}
			if (g2.MDL1(a,it2)<g1.MDL1(a,it)) { 
				g1=g2.copygraph();
				it=(ArrayList<Double>) it2.clone();
			}
		}
		BN b= new BN(g1, a, 0.5);
		return b;
	}




	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {

			double start=System.nanoTime();

			//		Amostrad ab = new Amostrad("bcancer.csv");
			//		Amostrad ad = new Amostrad("diabetes.csv");
			//		Amostrad ah = new Amostrad("hepatitis.csv");
			//		Amostrad at = new Amostrad("thyroid.csv");

			//		BN bc= aprende(ab, 3, 20);
			//		BN bh= aprende(ah, 4, 10);
			//		BN bd= aprende(ad, 4, 20);	
			//		BN bt= aprende(at, 10, 4);

			//		int[] td1= {1,2,0,1,0,0,1,0}; //n tem
			//		System.out.println(bd.probcancro(td1)*100+"%");

			//		int[] tc4= {1,0,2,3,2,0,1,2,1,1}; // tem
			//		System.out.println(bc.probcancro(tc4)*100+"%");

			//		int[] th1= {0,0,1,0,0,0,0,1,0,0,0,0,0,1,0,0,1,0,0}; // tem
			//		System.out.println(bh.probcancro(th1));


			//		int[] tt1= {2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,0}; // nao tem
			//		System.out.println("tt1: "+bt.probcancro(tt1)*100+"%");


			double end=System.nanoTime();
			System.out.println("t="+(end-start)*Math.pow(10, -9)/60+" min");
			System.out.println("t="+(end-start)*Math.pow(10, -9)+" seg");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
