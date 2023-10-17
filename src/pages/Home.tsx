import Header from '../components/Header'
import Sidebar from '../components/SideBar'
import Revisions from './admin/Revisions';

function Home() {
  return <div>
    <Header></Header>
    <Revisions></Revisions>
    {/* <Sidebar></Sidebar> */}
  </div>
}

export default Home;