package controller;

import model.*;
import model.sites.Hospital;
import model.sites.PoliceStation;
import model.sites.Site;

import java.util.*;

public class SiteManager {
    SparseMap sitemap;
    public SiteManager(SparseMap sitemap) {
        this.sitemap = sitemap;
    }

    /*
    Ersetzt in siteMap alle Knoten, deren Position einer Position aus positionList
    entspricht, mit einem Baustellenknoten. Dabei werden die Position und der valueWert des Knotens, der ersetzt wird, übernommen und das speedLimit wird auf 0
    gesetzt.
    Es können auch Nicht-Straßenknoten mit Baustellenknoten ersetzt werden.
     */
    void putSites(ArrayList<Position2D> positionList) {
        for (Position2D position : positionList) {
            Optional<BasicVertex> vertex = sitemap.getBasicVertex(position);
            if (vertex.isPresent()) {
                BasicVertex v = vertex.get();
                Site site = new Site(position.getRow(), position.getColumn(), v.getValue(), 0);
                sitemap.replaceVertex(position, site);
            }
        }
    }

    /*
    Sei S+ die Menge aller Straßenknoten aus siteMap, deren speedLimit-Wert größer
    als 0 ist. Sei z die Anzahl der Zusammenhangskomponenten, die von S+ gebildet
    werden.
    Die Methode gibt genau dann true zurück, wenn gilt: Ersetzt man alle Knoten, die
    sich auf einer angegebenen Position in positionList befinden, mit Baustellenknoten, dann erhöht z sich um mindestens 1.
    Die Methode soll sozusagen die Frage beantworten, ob die Straßenzüge der Stadt
    durch die entsprechenden Baustellen in noch mehr „Einzelteile“ zerhackt werden.
     */
    boolean isDisconnectedSet(ArrayList<Position2D> positionList) {
        Map<Position2D, BasicVertex> originalVertices = new HashMap<>();
        for (Position2D position : positionList) {
            Optional<BasicVertex> vertex = sitemap.getBasicVertex(position);
            if (vertex.isPresent()) {
                originalVertices.put(position, vertex.get());
                sitemap.replaceVertex(position, new Site(position.getRow(), position.getColumn(), vertex.get().getValue(), 0));
            }
        }

        int initialComponents = countComponents();
        for (Position2D position : positionList) {
            sitemap.replaceVertex(position, originalVertices.get(position));
        }
        int newComponents = countComponents();

        return newComponents > initialComponents;
    }

    /*
    Hilfsmethode für isDisconnectedSet(ArrayList<Position2D> positionList)
     */
    private int countComponents() {
        Set<BasicVertex> visited = new HashSet<>();
        int components = 0;

        for (BasicVertex[] row : sitemap.getVertexArray()) {
            for (BasicVertex v : row) {
                if (v instanceof BasicStreet && ((BasicStreet) v).getSpeedLimit() > 0 && !visited.contains(v)) {
                    components++;
                    bfs(v, visited);
                }
            }
        }
        return components;
    }

    /*
    Hilfsmethode für isDisconnectedSet(ArrayList<Position2D> positionList)
     */
    private void bfs(BasicVertex v, Set<BasicVertex> visited) {
        Queue<BasicVertex> q = new LinkedList<>();
        q.add(v);
        visited.add(v);

        while (!q.isEmpty()) {
            BasicVertex current = q.poll();
            for (BasicVertex neighbour : current.getNeighbours()) {
                if (neighbour instanceof BasicStreet && ((BasicStreet) neighbour).getSpeedLimit() > 0 && !visited.contains(neighbour)) {
                    visited.add(neighbour);
                    q.add(neighbour);
                }
            }
        }
    }

    /*
    Sei S+ die Menge aller Straßenknoten aus siteMap, deren speedLimit-Wert größer
    als 0 ist. Seien Z1, ..., Zn die Zusammenhangskomponenten, die von S+ gebildet werden. Diese Methode gibt genau dann true zurück, wenn für alle Zi
    , i = 1, ..., n, gilt,
    dass mindestens ein Straßenknoten a aus Zi zu einem Krankenhaus und mindestens
    ein Straßenknoten b aus Zi zu einer Polizeistation benachbart sind. Dabei dürfen a
    und b auch identisch sein.
    Sie dürfen davon ausgehen, dass es immer mindestens einen Straßenknoten in
    siteMap gibt. Diese Methode soll sozusagen die Frage beantworten, ob Sicherheit
    und Gesundheit der Bevölkerung trotz der entsprechend platzierten Baustellen, also
    nicht mehr befahrbaren Straßen, gewährleistet ist.
     */
    boolean hasValidSites() {
        List<List<BasicVertex>> components = getComponents();
        for (List<BasicVertex> component : components) {
            boolean hasHospital = false;
            boolean hasPoliceStation = false;
            for (BasicVertex v : component) {
                for (BasicVertex neighbour : v.getNeighbours()) {
                    if (neighbour instanceof Hospital) {
                        hasHospital = true;
                    }
                    if (neighbour instanceof PoliceStation) {
                        hasPoliceStation = true;
                    }
                }
                if (hasHospital && hasPoliceStation) {
                    break;
                }
            }
            if (!hasHospital || !hasPoliceStation) {
                return false;
            }
        }
        return true;
    }

    /*
    Hilfsmethode für hasValidSites()
     */
    private List<List<BasicVertex>> getComponents() {
        Set<BasicVertex> visited = new HashSet<>();
        List<List<BasicVertex>> components = new ArrayList<>();

        for (BasicVertex[] row : sitemap.getVertexArray()) {
            for (BasicVertex v : row) {
                if (v instanceof BasicStreet && ((BasicStreet) v).getSpeedLimit() > 0 && !visited.contains(v)) {
                    List<BasicVertex> newComponent = new ArrayList<>();
                    bfs2(v, visited, newComponent);
                    components.add(newComponent);
                }
            }
        }
        return components;
    }

    /*
    Hilfsmethode für hasValidSites()
     */
    private void bfs2(BasicVertex v, Set<BasicVertex> visited, List<BasicVertex> newComponent) {
        Queue<BasicVertex> q = new LinkedList<>();
        q.add(v);
        visited.add(v);
        newComponent.add(v);

        while (!q.isEmpty()) {
            BasicVertex current = q.poll();
            for (BasicVertex neighbour : current.getNeighbours()) {
                if (neighbour instanceof BasicStreet && ((BasicStreet) neighbour).getSpeedLimit() > 0 && !visited.contains(neighbour)) {
                    visited.add(neighbour);
                    q.add(neighbour);
                    newComponent.add(neighbour);
                }
            }
        }
    }

    public SparseMap getSitemap() {
        return sitemap;
    }

    public void setSitemap(SparseMap sitemap) {
        this.sitemap = sitemap;
    }

    /*
        Sortiert man aufsteigend einmal die value-Werte der Knoten aus a und einmal die
        value-Werte der Knoten aus b, so seien V (a) und V (b) die daraus resultierenden
        Zahlenfolgen. Ist V (a) lexikographisch kleiner als V (b), dann gibt die Methode a
        zurück, im umgekehrten Fall gibt die Methode b zurück. Sind beide lexikographisch
        gleich, so wird a zurück gegeben.
         */
    ArrayList<BasicVertex> getMinLexi(ArrayList<BasicVertex> a, ArrayList<BasicVertex> b) {
        List<Integer> valuesA = new ArrayList<>();
        for (BasicVertex v : a) {
            valuesA.add(v.getValue());
        }
        Collections.sort(valuesA);

        List<Integer> valuesB = new ArrayList<>();
        for (BasicVertex v : b) {
            valuesB.add(v.getValue());
        }
        Collections.sort(valuesB);

        for (int i = 0; i < Math.min(valuesA.size(), valuesB.size()); i++) {
            int compare = Integer.compare(valuesA.get(i), valuesB.get(i));
            if (compare < 0) {
                return a;
            } else if (compare > 0) {
                return b;
            }
        }
        return a;
    }



    /*
    Diese Methode berechnet eine Menge M von Straßenknoten, die folgende Bedingungen erfüllt:
    (1) M enthält höchstens k Straßenknoten.
    (2) Die Straßenknoten in M sind untereinander nicht benachbart.
    (3) Ersetzt man alle Straßenknoten, die in M gespeichert sind, in siteMap mit
    Grünflächen, so erhöht sich die Anzahl der BasicStreet-Zusammenhangskomponenten in siteMap von 1 auf mindestens 2.
    (4) Sortiert man alle Knoten-Mengen, welche die vorgenannten Bedingungen erfüllen, aufsteigend nach value-Wert der Knoten, dann ist die für M resultierende
    Folge von value-Werten die lexikographisch kleinste.
    (5) M ist aufsteigend nach value-Wert der Knoten sortiert.
    Gibt es keine solche Menge M, dann wird ein leeres Array zurückgegeben.
    Diese Methode beantwortet sozusagen die Frage, wie eine Menge von k nicht
    miteinander verbundenen Straßenknoten aussehen kann, die dafür sorgt, dass das
    zuvor zusammenhängende Straßennetz nun unzusammenhängend ist. Sie dürfen davon ausgehen, dass beim Testen dieser Methode 0 ≤ k ≤ 5 gilt, dass es nie mehr
    als 15 Straßenknoten in siteMap geben wird und dass die Anzahl an Zusammenhangskomponenten in siteMap bei Aufruf dieser Methode 1 ist.
     */
    BasicStreet[] getCutSet(int k) {
        List<BasicStreet> allStreets = new ArrayList<>();
        for (BasicVertex[] row : sitemap.getVertexArray()) {
            for (BasicVertex v : row) {
                if (v instanceof BasicStreet) {
                    allStreets.add((BasicStreet) v);
                }
            }
        }

        List<List<BasicStreet>> validCombinations = new ArrayList<>();
        generateCombinations(allStreets, new ArrayList<>(), validCombinations, k, 0);

        List<BasicStreet> bestCombination = null;

        for (List<BasicStreet> combination : validCombinations) {
            if (isValidCutSet(combination)) {
                if (bestCombination == null) {
                    bestCombination = combination;
                } else {
                    ArrayList<BasicVertex> best = new ArrayList<>(bestCombination);
                    ArrayList<BasicVertex> current = new ArrayList<>(combination);
                    bestCombination = (List<BasicStreet>) (List<?>) getMinLexi(best, current);
                }
            }
        }
        if (bestCombination == null) {
            return new BasicStreet[0];
        }
        bestCombination.sort(Comparator.comparingInt(BasicVertex::getValue));
        return new BasicStreet[0];

    }


    /*
    Hilfsmethode für getCutSet(int k)
     */
    private void generateCombinations(List<BasicStreet> allStreets, List<BasicStreet> current, List<List<BasicStreet>> validCombinations, int k, int start) {
        if (!current.isEmpty() && current.size() <= k) {
            validCombinations.add(new ArrayList<>(current));
        }
        for (int i = start; i < allStreets.size(); i++) {
            current.add(allStreets.get(i));
            generateCombinations(allStreets, current, validCombinations, k, i + 1);
            current.removeLast();
        }
    }

    /*
    Hilfsmethode für getCutSet(int k)
     */
    private boolean isValidCutSet(List<BasicStreet> combination) {
        Set<BasicVertex> visited = new HashSet<>();
        for (BasicStreet street : combination) {
            for (BasicVertex neighbour : street.getNeighbours()) {
                if (combination.contains(neighbour)) {
                    return false;
                }
            }
        }
        for (BasicStreet street : combination) {
            sitemap.replaceVertex(street.getPosition(), new BasicGreen(street.getPosition().getRow(), street.getPosition().getColumn(), street.getValue()));
        }

        boolean result = countComponents() > 1;

        for (BasicStreet street : combination) {
            sitemap.replaceVertex(street.getPosition(), street);
        }

        return result;
    }

}
