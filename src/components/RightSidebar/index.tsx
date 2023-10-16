import {
  TabComponent,
  TabItemsDirective,
  TabItemDirective,
} from "@syncfusion/ej2-react-navigations"
import { ListViewComponent } from "@syncfusion/ej2-react-lists";
import "./style.css"

interface linksDataPorps {
  title: string;
  content: string;
  icon: string;
  category: string;
}

const RightSidebar = () => {
  const linksData = [
    {
      title: "Helpdesk",
      content:
        "The fastest way to get help from IMS! Tell us about your problem so we can help ASAP!",
      icon: "circle-info bg-gray",
      category: "Useful Links",
    },
    {
      title: "Guidelines",
      content:
        "Step-by-Step video to guide you through the core workflows and comprehensive documentation of QS 2.0",
      icon: "bookmark-fill bg-green",
      category: "Useful Links",
    },
    {
      title: "Forms, Templates & <br>Documents",
      content: "Common forms for download",
      icon: "border-box bg-blue",
      category: "Useful Links",
    },
    {
      title: "Tips",
      content: "Information and tips",
      icon: "comment-add bg-purple",
      category: "Useful Links",
    },
    {
      title: "UCC List",
      content: "UCC List",
      icon: "list-unordered bg-yellow",
      category: "Useful Links",
    },
    {
      title: "Approval System",
      content:
        "To Review and Enquiry Subcontract, Procurement, Subcontract Appraisal Approvals",
      icon: "check bg-red",
      category: "Useful Systems",
    },
    {
      title: "Gammon ERP Portal",
      content: "Gammon ERP Portal",
      icon: "settings bg-yellow",
      category: "Useful Systems",
    },
    {
      title: "JDE",
      content: "Oracle JD Edwards",
      icon: "form-field bg-green",
      category: "Useful Systems",
    },
    {
      title: "Business Management System",
      content:
        "Documents for Pre-Contract, Project Delivery, Group Wide and Business Support",
      icon: "page-columns bg-cyan",
      category: "Useful Systems",
    },
    {
      title: "Other Gammon Systems",
      content: "Full list of Gammon Systems",
      icon: "plus-small bg-yellow",
      category: "Useful Systems",
    },
  ];
  const fields = { text: "Name", groupBy: "category" };
  const listTemplate = (data: linksDataPorps) => {
    return (
      <div className="e-list-wrapper e-list-multi-line e-list-avatar">
        <div className={`${data.icon} e-avatar e-avatar-circle`}>
          <span className={`e-icons e-${data.icon} e-large color-white`}></span>
        </div>
        <span className="e-list-item-header">{data.title}</span>
        <span className="e-list-content">{data.content}</span>
      </div>
    );
  };
  const groupTemplate = (data: any) => {
    return (
      <div className="e-list-wrapper">
        <span className="e-list-item-content">{data.items[0].category}</span>
      </div>
    );
  };
  const linksContent = () => {
    return (
      <ListViewComponent
        id="linksList"
        dataSource={linksData}
        fields={fields}
        cssClass="e-list-template"
        template={listTemplate}
        groupTemplate={groupTemplate}
      ></ListViewComponent>
    );
  }
  const accountCodeContent = () => {
    return <div>Account Code</div>
  }
  const addressBookContent = () => {
    return <div>Address Book</div>
  }

  return (
    <TabComponent width={250} cssClass="right-sidebar">
      <TabItemsDirective>
        <TabItemDirective
          header={{ text: "Links" }}
          content={linksContent}
        ></TabItemDirective>
        <TabItemDirective
          header={{ text: "Account Code" }}
          content={accountCodeContent}
        ></TabItemDirective>
        <TabItemDirective
          header={{ text: "Address Book" }}
          content={addressBookContent}
        ></TabItemDirective>
      </TabItemsDirective>
    </TabComponent>
  );
}

export default RightSidebar
