package cc.sportsdb.collector.spider;

public final class Tables {

    private Table[] tables;

    public Tables(Table... tables) {
        this.tables = tables;
    }

    public Table[] getTables() {
        return tables;
    }

    public static class Table {
        private String query;
        private Column[] columns;

        public Table(String query, Column[] columns) {
            this.query = query;
            this.columns = columns;
        }

        public String getQuery() {
            return query;
        }

        public Column[] getColumns() {
            return columns;
        }
    }

    public static class Column {
        private String name;
        private String query;

        public Column(String name, String query) {
            this.name = name;
            this.query = query;
        }

        public String getName() {
            return name;
        }

        public String getQuery() {
            return query;
        }

    }
}
