import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PostRepositoryImpl implements IRepository<Post> {
    private Set<Post> postsCached = new HashSet<>();
    private Set<User> usersCached = new HashSet<>();
    private java.sql.Connection con;
    public PostRepositoryImpl(){
        this.con = SocialNetworkService.getConnection();
    }

    /**
     * Se encarga de mapear um registro de la base de datos para converirlo en un objeto Post
     * @param rs
     * @return Un objeto Post
     * @throws SQLException
     */
    public Post bdToEntity(ResultSet rs) throws SQLException {
        User user =  getUserCached(rs.getInt("userId"));
        if (user == null ){
            user = new UserRepositoryImpl().findById(rs.getInt("userId"));
            usersCached.add(user);
            user.setPosts(findByUser(user));
        }
        Post p = new Post(rs.getInt("id"),
                rs.getString("text"),
                rs.getInt("likes"),
                rs.getDate("date"),
                user
        );
        postsCached.add(p);
        return p;
    }

    /**
     * Consulta todos los registros de la tabla posts
     * @return Una lista de objetos Post
     * @throws SQLException
     */
    public List<Post> findAll() throws SQLException {

        List<Post> posts = new ArrayList<>();

        Statement st = this.con.createStatement();
        //Ejecutar la consulta, guardando los datos devueltos en un Resulset
        ResultSet rs = st.executeQuery("SELECT * FROM posts ORDER BY date DESC");

        while(rs.next()){
            //Mapeamos el registro de la BD en un post
            Post post = getCached(rs.getInt(1));
            if (post == null) {
                post = bdToEntity(rs);
                //Añadir el User al conjunto de posts
                posts.add(post);
            }
        }
        return posts;
    }
    private Post getCached(int id){
        for (Post post : postsCached){
            if (post.getId() == id) return post;
        }
        return null;
    }
    private User getUserCached(int i){
        for(User user : usersCached){
            if (user.getId() == i) return user;
        }
        return null;
    }
    /**
     * Consulta todos los registros de la tabla posts
     * @return Una lista de objetos Post
     * @throws SQLException
     */
    public List<Post> findByUser(User user) throws SQLException {

        List<Post> posts = new ArrayList<>();

        PreparedStatement st = this.con.prepareStatement("SELECT * FROM posts WHERE userId = ? ORDER BY date DESC");
        st.setInt(1, user.getId());

        ResultSet rs = st.executeQuery();

        while(rs.next()){
            Post post = getCached(rs.getInt(1));
            if (post == null) {
                post = bdToEntity(rs);
            }
            //Añadir el Post al conjunto de posts
            posts.add(post);

        }
        return posts;
    }

    /**
     * Busca un post por id en la tabla posts
     * @param id
     * @return El objeto Post o null si no existe
     * @throws SQLException
     */
    public Post findById(int id) throws SQLException {
        Post post = getCached(id);
        if (post != null) {
            return post;
        }
        PreparedStatement st = con.prepareStatement("SELECT * FROM posts WHERE id = ? ");
        st.setInt(1, id);

        ResultSet rs = st.executeQuery();

        //Si la consulta devuelve algún resultado ...
        if (rs.next()){
            // ... lo mapeamos a un objeto Post
            post = bdToEntity(rs);
        }
        //Devolvemos el Post ya mapeado
        return post;
    }
    /**
     * Guarda el post Post en la base de datos, insertando si id es distinto de -1 o actualizando aquél
     * Sólo se pueden crear post no actualizar
     * registro con dicho id
     * @param post
     * @throws SQLException
     */
    public void save(Post post) throws SQLException{
        if (post.getId() == -1){
            ResultSet rs;
            PreparedStatement st = null;
            String query = "INSERT INTO posts (text, likes, date, userId) VALUES (?, ?, ?, ?)";
            //Fijáos en Statement.RETURN_GENERATED_KEYS. Permite recuperar el campo ID autogenerado por MySql
            st = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            st.setString(1, post.getText());
            st.setInt(2, post.getLikes());
            st.setDate(3, post.getDate());
            st.setInt(4, post.getUser().getId());

            st.executeUpdate();

            //Recuperar el id autogenerado
            rs = st.getGeneratedKeys();
            //Este ResultSet solo puede contener un registro: el ID autogenerado

            if (rs.next()){
                //Ahora ya sabemos cuál es el nuevo id del Usuario
                post.setId(rs.getInt(1));
                System.out.println("Autogenerated ID:  " + post.getId());
            }
        }
    }

    /**
     * Elimina de la base de datos el Post post
     * @param post
     * @throws SQLException
     */
    public void delete(Post post) throws SQLException {
        PreparedStatement st = con.prepareStatement("DELETE FROM posts WHERE id = ?");
        st.setInt(1, post.getId());
        st.executeUpdate();
        st.close();
    }
}
