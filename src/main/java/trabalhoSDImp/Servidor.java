package trabalhoSDImp;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;

import trabalhoSD.GrafoInterface;

/**
 * 
 * Tafarel Agusto
 * 
 * @author Tafarel
 *
 */
public class Servidor {
	
    public static GrafoImpl grafoImpl;
    @SuppressWarnings("rawtypes")
    public static GrafoInterface.Processor processor;    
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public static void main(String args[]){
        try{
            grafoImpl = new GrafoImpl();
            processor = new GrafoInterface.Processor(grafoImpl);
            int porta = 9090;
            
            if(args.length != 0)
                porta = Integer.parseInt(args[0]);

            TServerTransport serverTransport = new TServerSocket(porta);            
            
            TServer server = new TThreadPoolServer(new TThreadPoolServer.Args(serverTransport).processor(processor));
            
            System.out.println("Iniciando servidor na porta " + porta);
            System.out.println("Servidor aberto com sucesso!");
            System.out.println("Aguardando conexões...");     
            server.serve();
        }
        catch (Exception x){
            System.out.println("Não foi possível iniciar o servidor!");
            x.printStackTrace();
        }
    }

}
