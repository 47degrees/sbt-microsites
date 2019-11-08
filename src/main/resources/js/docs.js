/**
 * Toggle an specific class to the received DOM element.
 * @param {string}	elemSelector The query selector specifying the target element.
 * @param {string}	[activeClass='active'] The class to be applied/removed.
 */
function toggleClass(elemSelector, activeClass = 'active') {
	const elem = document.querySelector(elemSelector);
  if (elem) {
    elem.classList.toggle(activeClass);
  }
}

/**
 * Toggle specific classes to an array of corresponding DOM elements.
 * @param {Array<string>}	elemSelectors The query selectors specifying the target elements.
 * @param {Array<string>}	activeClasses The classes to be applied/removed.
 */
function toggleClasses(elemSelectors, activeClasses) {
  elemSelectors.map((elemSelector, idx) => {
		toggleClass(elemSelector, activeClasses[idx]);
	});
}

/**
 * Remove active class from siblings DOM elements and apply it to event target.
 * @param {Element}		element The element receiving the class, and whose siblings will lose it.
 * @param {string}		[activeClass='active'] The class to be applied.
 */
function activate(element, activeClass = 'active') {
	[...element.parentNode.children].map((elem) => elem.classList.remove(activeClass));
	element.classList.add(activeClass);
}

/**
 * Remove active class from siblings parent DOM elements and apply it to element target parent.
 * @param {Element}		element The element receiving the class, and whose siblings will lose it.
 * @param {string}		[activeClass='active'] The class to be applied.
 */
function activateParent(element, activeClass = 'active') {
	const elemParent = element.parentNode;
	activate(elemParent, activeClass);
}

/**
 * Remove active class from siblings parent DOM elements and apply it to element target parent.
 * @param {Element}		element The element receiving the class, and whose siblings will lose it.
 * @param {string}		[activeClass='active'] The class to be applied.
 */
function toggleParent(element, activeClass = 'active') {
	const elemParent = element.parentNode;
	if (elemParent) {
		elemParent.classList.toggle(activeClass);
	}
}


/**
 * dfidhfidhfidhfdfhdifhd
 */


function activateToggle() {
  console.log('activateToggle');
  const menuToggles = document.querySelectorAll('#menu-toggle, #main-toggle');
  [...menuToggles].map(elem => {
    elem.onclick = (e) => {
      e.preventDefault();
      console.log('click');
      toggleClass('#wrapper', 'toggled');
    }
  })
}


window.addEventListener('DOMContentLoaded', (event) => {
  console.log('DOM fully loaded and parsed');
  activateToggle();

  const menuParents = document.querySelectorAll('.drop-nested');
  console.log(menuParents);
  [...menuParents].map(elem => {
    elem.onclick = (e) => {
      e.preventDefault();
			toggleParent(elem, 'open');
			const elementType = e.currentTarget.tagName.toLowerCase();
			console.log(elementType);
			if (elementType === 'a') {
				console.log('This is a link');
				const destination = e.currentTarget.href;
				console.log(destination);
				window.location.href = destination;
			}
    }
  })
});
