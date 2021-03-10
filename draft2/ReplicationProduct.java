
import java.util.*;

public class ReplicationProduct {
    
    private Sequence template;
    private Sequence primer;
    private ArrayList<Integer> matchLocations;
    private ArrayList<String> matchProducts;
    private boolean analyzed;
    
    public ReplicationProduct(Sequence template, Sequence primer) {

        this.template = template;
        this.primer = primer;

        matchLocations = new ArrayList<Integer>();
        matchProducts = new ArrayList<String>();

        analyzed = false;
    }

    public boolean isEmpty() {
        return matchLocations.isEmpty();
    }

    public boolean isAnalyzed() {
        return analyzed;
    }

    public int numMatches() {
        return matchLocations.size();
    }

    public int getMatchLocation(int i) {
        return matchLocations.get(i);
    }

    public String getMatchProduct(int i) {
        return matchProducts.get(i);
    }

    public Sequence getTemplate() {
        return template;
    }

    public Sequence getPrimer() {
        return primer;
    }

    public void findAllMatches() {
        
        String text = template.complement();
        String pattern = primer.sequence();
        
        matchLocations = Matcher.findMatches(text, pattern);

        for (int i = 0; i < matchLocations.size(); i++) {
            int location = matchLocations.get(i);
            String product = text.substring(location);
            matchProducts.add(product);
        }

        analyzed = true;
    }

}
