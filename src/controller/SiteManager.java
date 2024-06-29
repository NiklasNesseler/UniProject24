package controller;

import model.*;
import model.sites.Hospital;
import model.sites.PoliceStation;
import model.sites.Site;

import java.util.*;
import java.util.stream.Collectors;

public class SiteManager {
    SparseMap siteMap;
    public SiteManager(SparseMap siteMap) {
        this.siteMap = siteMap;
    }

    /*
    Ersetzt in siteMap alle Knoten, deren Position einer Position aus positionList
    entspricht, mit einem Baustellenknoten. Dabei werden die Position und der valueWert des Knotens, der ersetzt wird, übernommen und das speedLimit wird auf 0
    gesetzt.
    Es können auch Nicht-Straßenknoten mit Baustellenknoten ersetzt werden.
     */
    public void putSites(ArrayList<Position2D> positionList) {
        for (Position2D position : positionList) {
            Optional<BasicVertex> vertex = siteMap.getBasicVertex(position);
            if (vertex.isPresent()) {
                BasicVertex v = vertex.get();
                Site site = new Site(position.getRow(), position.getColumn(), v.getValue(), 0);
                site.setContainingMap(siteMap);
                siteMap.replaceVertex(position, site);
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
    public boolean isDisconnectedSet(ArrayList<Position2D> positionList) {
        Map<Position2D, BasicVertex> originalVertices = new HashMap<>();
        for (Position2D position : positionList) {
            Optional<BasicVertex> vertex = siteMap.getBasicVertex(position);
            if (vertex.isPresent()) {
                originalVertices.put(position, vertex.get());
                Site site = new Site(position.getRow(), position.getColumn(), vertex.get().getValue(), 0);
                site.setContainingMap(vertex.get().getContainingMap());
                siteMap.replaceVertex(position, site);
            }
        }



        int initialComponents = countComponents();
        for (Position2D position : positionList) {
            siteMap.replaceVertex(position, originalVertices.get(position));
        }
        int newComponents = countComponents();



        //Fragt mich nicht warum aber das funktioniert
        return newComponents < initialComponents;
    }



    /*
    Hilfsmethode für isDisconnectedSet(ArrayList<Position2D> positionList)
     */
    private int countComponents() {
        Set<BasicVertex> visited = new HashSet<>();
        int components = 0;

        for (BasicVertex[] row : siteMap.getSparseVertexArray()) {
            for (BasicVertex v : row) {
                if (v instanceof BasicStreet && ((BasicStreet) v).getSpeedLimit() != 0 && !visited.contains(v)) {
                    List<BasicVertex> component = new ArrayList<>();
                    dfs(v, visited, component);
                    if (!component.isEmpty()) {
                        components++;
                    }
                }
            }
        }
        return components;
    }

    /*
    Hilfsmethode für isDisconnectedSet(ArrayList<Position2D> positionList)
     */
    private void dfs(BasicVertex v, Set<BasicVertex> visited, List<BasicVertex> component) {
        visited.add(v);
        component.add(v);

        for (BasicVertex neighbour : v.getNeighbours()) {
            if (neighbour instanceof BasicStreet && ((BasicStreet) neighbour).getSpeedLimit() != 0 && !visited.contains(neighbour)) {
                dfs(neighbour, visited, component);
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
    public boolean hasValidSites() {
        List<List<BasicVertex>> components = getComponents();
        for (int i = 0; i < components.size(); i++) {
            boolean hasHospital = false;
            boolean hasPoliceStation = false;
            for (BasicVertex v : components.get(i)) {
                for (BasicVertex neighbour : v.getNeighbours()) {
                    if (neighbour instanceof Hospital) {
                        hasHospital = true;
                    }
                    if (neighbour instanceof PoliceStation) {
                        hasPoliceStation = true;
                    }
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
    List<List<BasicVertex>> getComponents() {
        Set<BasicVertex> visited = new HashSet<>();
        List<List<BasicVertex>> components = new ArrayList<>();

        for (BasicVertex[] row : siteMap.getSparseVertexArray()) {
            for (BasicVertex v : row) {
                //TODO: das ungleich null unbedingt noch in > 0 ändern
                if (v instanceof BasicStreet && ((BasicStreet) v).getSpeedLimit() != 0 && !visited.contains(v)) {
                    List<BasicVertex> newComponent = new ArrayList<>();
                    dfs(v, visited, newComponent);
                    components.add(newComponent);
                }
            }
        }
        return components;
    }

    public SparseMap getSiteMap() {
        return siteMap;
    }

    public void setSiteMap(SparseMap siteMap) {
        this.siteMap = siteMap;
    }

    /*
        Sortiert man aufsteigend einmal die value-Werte der Knoten aus a und einmal die
        value-Werte der Knoten aus b, so seien V (a) und V (b) die daraus resultierenden
        Zahlenfolgen. Ist V (a) lexikographisch kleiner als V (b), dann gibt die Methode a
        zurück, im umgekehrten Fall gibt die Methode b zurück. Sind beide lexikographisch
        gleich, so wird a zurück gegeben.
         */
    public ArrayList<BasicVertex> getMinLexi(ArrayList<BasicVertex> a, ArrayList<BasicVertex> b) {
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

        if (valuesA.size() < valuesB.size()) {
            return a;
        }else  if (valuesB.size() < valuesA.size()) {
            return b;
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
    public BasicStreet[] getCutSet(int k) {
        List<BasicStreet> allStreets = getAllStreets();
        if (allStreets.size() > 15) {
            throw new IllegalStateException("There should never be more than 15 road nodes in siteMap.");
        }

        List<List<BasicStreet>> combinations = new ArrayList<>();


        List<BasicStreet> minLexiSet = null;
        int originalComponents = countComponents();
        generateCombinations(allStreets, new ArrayList<>(), combinations, k, originalComponents);

        for (List<BasicStreet> combo : combinations) {
            if (isValidCutSet(combo, originalComponents)) {
                if (minLexiSet == null || isLexicographicallySmaller(combo, minLexiSet)) {
                    minLexiSet = new ArrayList<>(combo);
                }
            }
        }

        if (minLexiSet == null) {
            return new BasicStreet[0];
        }

        minLexiSet.sort(Comparator.comparingInt(BasicVertex::getValue));

        return minLexiSet.toArray(new BasicStreet[0]);
    }

    private List<BasicStreet> getAllStreets() {
        List<BasicStreet> streets = new ArrayList<>();
        for (BasicVertex[] row : siteMap.getSparseVertexArray()) {
            for (BasicVertex vertex : row) {
                if (vertex instanceof BasicStreet) {
                    streets.add((BasicStreet) vertex);
                }
            }
        }
        return streets;
    }


    /*
    Hilfsmethode für getCutSet(int k)
     */
    private void generateCombinations(List<BasicStreet> allStreets, List<BasicStreet> current, List<List<BasicStreet>> combinations, int k,  int originalComponents) {
        if (!current.isEmpty() && current.size() <= k) {
            combinations.add(new ArrayList<>(current));
        }
        if (current.size() == k) {
            return;
        }
        for (int i = 0; i < allStreets.size(); i++) {
            BasicStreet street = allStreets.get(i);
            if (current.isEmpty() || !current.contains(street)) {
                List<BasicStreet> newCurrent = new ArrayList<>(current);
                newCurrent.add(allStreets.get(i));
                if (isStreetEffective(street, originalComponents)) {
                    generateCombinations(allStreets, newCurrent, combinations, k, originalComponents);
                }
            }
        }
    }

    private boolean isStreetEffective(BasicStreet street, int originalComponents) {
        siteMap.replaceVertex(street.getPosition(), new BasicGreen(street.getPosition().getRow(), street.getPosition().getColumn(), street.getValue()));
        int newComponents = countComponents();
        siteMap.replaceVertex(street.getPosition(), street);

        return newComponents > originalComponents;
    }

    /*
    Hilfsmethode für getCutSet(int k)
     */
    private boolean isValidCutSet(List<BasicStreet> combo, int originalComponents) {
        // Temporarily replace streets with greens
        Map<BasicStreet, BasicVertex> originalVertices = new HashMap<>();
        for (BasicStreet street : combo) {
            originalVertices.put(street, siteMap.getBasicVertex(street.getPosition()).orElse(null));
            siteMap.replaceVertex(street.getPosition(), new BasicGreen(street.getPosition().getRow(), street.getPosition().getColumn(), street.getValue()));
        }

        int newComponents = countComponents2();

        for (Map.Entry<BasicStreet, BasicVertex> entry : originalVertices.entrySet()) {
            siteMap.replaceVertex(entry.getKey().getPosition(), entry.getValue());
        }

        boolean isEffectiveCut = newComponents > originalComponents;
        return isEffectiveCut;
    }

    private boolean isLexicographicallySmaller(List<BasicStreet> combo1, List<BasicStreet> combo2) {
        for (int i = 0; i < Math.min(combo1.size(), combo2.size()); i++) {
            if (combo1.get(i).getValue() != combo2.get(i).getValue()) {
                return combo1.get(i).getValue() < combo2.get(i).getValue();
            }
        }
        return combo1.size() < combo2.size();
    }

    int countComponents2() {
        Set<BasicVertex> visited = new HashSet<>();
        int components = 0;

        for (int i = 0; i < siteMap.getSparseVertexArray().length; i++) {
            for (int j = 0; j < siteMap.getSparseVertexArray()[i].length; j++) {
                BasicVertex vertex = siteMap.getSparseVertexArray()[i][j];
                if (vertex instanceof BasicStreet && !visited.contains(vertex)) {

                    dfs(vertex, visited);
                    components++;
                }
            }
        }
        return components;
    }


    private void dfs(BasicVertex vertex, Set<BasicVertex> visited) {

        visited.add(vertex);

        for (BasicVertex neighbor : vertex.getNeighbours()) {
            if (neighbor instanceof BasicStreet && !visited.contains(neighbor)) {
                dfs(neighbor, visited);
            }
        }
    }
}

