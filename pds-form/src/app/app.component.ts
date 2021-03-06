import { Component, ComponentRef, ViewChild, ViewEncapsulation } from '@angular/core';
import { FormComponent as Form4 } from 'projects/form4/src/app/form/form.component';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class AppComponent {
  title = 'pds-form';
  mode: string = 'h';
  modeDesc = {h: 'HTML', f: 'Freemarker'};
  component: ComponentRef<any>;
  bypassCss = ['.appContainer'];
  cssOffset = '\t\t\t';
  dataOffset = 2;
  toggle():Promise<void> {
    return new Promise((resolve) => {
      this.component['mode'] = this.component['mode'] == 'h' ? 'f' : 'h';
      this.mode = this.component['mode'];
      resolve();
    });
  }

  onActivate(component: ComponentRef<any>){
    this.mode = component['mode'];
    this.component = component;
  }

  process(str) {
    var div = document.createElement('div');
    div.innerHTML = str.trim();

    return this.format(div, this.dataOffset).innerHTML;
  }

  format(node: Element, level: number) {
    var indentBefore = new Array(level++ + 1).join('\t'),
      indentAfter = new Array(level - 1).join('\t'),
      textNode;

    for (var i = 0; i < node.children.length; i++) {
      textNode = document.createTextNode('\n' + indentBefore);
      node.insertBefore(textNode, node.children[i]);

      this.format(node.children[i], level);

      if (node.lastElementChild == node.children[i]) {
        textNode = document.createTextNode('\n' + indentAfter);
        node.appendChild(textNode);
      }
    }

    return node;
  }


  download() {
    const needToggle = this.component['mode'] == 'h';
    if(needToggle) this.toggle();
    setTimeout(() =>{
      const element = document.createElement('a');
      const formElement = document.getElementById(this.component['id']);
      let content:string = this.component['html'];
      let data = formElement.outerHTML;
      data = data.replace(/\[#--/g, '<!--');
      data = data.replace(/--#\]/g, '-->');
      data = data.replace(/(\[#[\s\S]*?\])/g, '<!-- # $1 # -->');
      data = data.replace(/(\[\/#[\s\S]*?\])/g, '<!-- # $1 # -->');
      data = this.process(data);
      data = data.replace(/<!-- # /g, '');
      data = data.replace(/ # -->/g, '');
      data = data.replace(/\☐/g, '&#9744;');
      data = data.replace(/\☑/g, '&#9745;');
      data = data.replace(/<!---->/g, '');
      data = data.replace(/<!--bindings=[\s\S]*?-->/gm, '');
      data = data.replace(/&amp;nbsp;/g, '&nbsp;');
      const styles = document.styleSheets;
      let css = '';
      for(var styleId = 0; styleId < styles.length; styleId++){
        if(styles[styleId]['rules'].length > 0){
          for(var ruleId = 0; ruleId < styles[styleId]['rules'].length; ruleId++){
            if(this.bypassCss.indexOf(styles[styleId]['rules'][ruleId]['selectorText']) < 0){
              css += this.cssOffset + styles[styleId]['rules'][ruleId].cssText + "\n";
            }
          }
        }
      }
      content = content.replace(/\s{4,4}\[#-- style --#\]/, css);
      content = content.replace(/\[#-- data --#\]/, data);
      element.setAttribute('href', 'data:text/plain;charset=utf-8,' + encodeURIComponent(content));
      element.setAttribute('download', this.component['filename'] || 'filename.txt');

      element.style.display = 'none';
      document.body.appendChild(element);

      element.click();

      document.body.removeChild(element);
    }, 200);
  }

}
