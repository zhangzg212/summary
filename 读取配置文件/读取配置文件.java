InputStream in = this.getClass().getResourceAsStream("/test.properties");
Properties props = new Properties();
InputStreamReader inputStreamReader = new InputStreamReader(in, "UTF-8");
props.load(inputStreamReader);
props.getProperty(key);

