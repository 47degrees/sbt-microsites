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
 * dfidhfidhfidhfdfhdifhd
 */


function activeToggle() {
  console.log('activeToggle');
  const menuToggles = document.querySelectorAll('#menu-toggle, #main-toggle');
  console.log(menuToggles);
  [...menuToggles].map(elem => {
    elem.onclick = (e) => {
      e.preventDefault();
      console.log('click');
      toggleClass('#wrapper', 'toggled');
    }
  })
}

/**
 * This function generates the “unrolling” of the secction by adding
 * some classes to the element and applying a jQuery slide action
 *
 * @param el The DOM element on which to perform the action
 * @param speed The desired speed to slide up/down the section
 */
// function activate(el, speed) {
//   if (!el.parent().hasClass('active')) {
//     $('.sidebar-nav li ul').slideUp(speed);
//     el.next().slideToggle(speed);
//     $('.sidebar-nav li').removeClass('active');
//     el.parent().addClass('active');
//   } else {
//     el.next().slideToggle(speed);
//     $('.sidebar-nav li').removeClass('active');
//   }
// }


window.addEventListener('DOMContentLoaded', (event) => {
  console.log('DOM fully loaded and parsed');
  activeToggle();

  const menuParents = document.querySelectorAll('.drop-nested');
  console.log(menuParents);
  [...menuParents].map(elem => {
    elem.onclick = (e) => {
      e.preventDefault();
      console.log('click menu item');
      activateParent(elem, 'open');
    }
  })

});


// /**
//  * This function generates the “unrolling” of the secction by adding
//  * some classes to the element and applying a jQuery slide action
//  *
//  * @param el The DOM element on which to perform the action
//  * @param speed The desired speed to slide up/down the section
//  */
// function activate(el, speed) {
//   if (!el.parent().hasClass('active')) {
//     $('.sidebar-nav li ul').slideUp(speed);
//     el.next().slideToggle(speed);
//     $('.sidebar-nav li').removeClass('active');
//     el.parent().addClass('active');
//   } else {
//     el.next().slideToggle(speed);
//     $('.sidebar-nav li').removeClass('active');
//   }
// }
//
// On click slide down or up the links section
// $('.drop-nested').click(function(e) {
//   e.preventDefault();
//   activate($(this), 300);
// });

// This detects the path to activate the current link accordingly
var current = location.pathname;
$('.sidebar-nav > li > ul a').each(function() {
  var $this = $(this);

  // If the current path is like this link, make it active
  if ($this.attr('href') === current) {
    $this.addClass('active');
    activate($this.closest('.sidebar-nav > li').children('a'), 0);
  }
})

$('.sidebar-nav > li > a').each(function() {
  var $this = $(this);

  // If the current path is like this link, make it active
  if ($this.attr('href') === current) {
    $this.addClass('active');
    activate($this.closest('.sidebar-nav > li').children('a'), 0);
  }
})
