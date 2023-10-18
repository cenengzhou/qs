import Header from '../components/Header'
import Sidebar from '../components/SideBar'
import Revisions from './admin/Revisisons';

function Home() {
  return (
    <div>
      <Header></Header>
      <div className="">
        <Sidebar></Sidebar>
      </div>
    </div>
  );
}

export default Home;